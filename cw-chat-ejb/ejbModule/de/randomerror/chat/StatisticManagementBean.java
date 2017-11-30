package de.randomerror.chat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.randomerror.chat.interfaces.BroadcastingLocal;
import de.randomerror.chat.interfaces.StatisticManagementLocal;
import de.randomerror.chat.interfaces.StatisticManagementRemote;

@Stateless
public class StatisticManagementBean implements StatisticManagementLocal, StatisticManagementRemote {
	
	private static final String HALF_HOUR_TIMER_INFO = "halfHourTimer";
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject
	private BroadcastingLocal broadcast;
	
	@Resource
	private TimerService timerService;
	
	@PostConstruct
	public void init() {
		try {
			getCurrent();
		} catch(NoResultException e) {
			CommonStatistic current = new CommonStatistic();
			current.setStartingDate(new Date());
			entityManager.persist(current);
		}
	}
	
	private CommonStatistic getCurrent() {
		return entityManager.createNamedQuery("CommonStatistic.findMostRecent", CommonStatistic.class).getSingleResult();
	}
	
	@Override
	public List<CommonStatistic> getStatistics() {
		TypedQuery<CommonStatistic> q = entityManager.createNamedQuery("CommonStatistic.findAll", CommonStatistic.class);
		return q.getResultList();
	}
	
	@Schedule(minute="*", hour="*")
	public void newCommonStatistic(Timer timer) {
		CommonStatistic current = getCurrent();
		entityManager.lock(current, LockModeType.PESSIMISTIC_WRITE);
		current.setEndDate(new Date());
		
		entityManager.merge(current);
		
		CommonStatistic newStat = new CommonStatistic();
		newStat.setStartingDate(new Date());
		
		entityManager.persist(newStat);
	}
	
	@Override
	public void newLogin() {
		CommonStatistic current = getCurrent();
		entityManager.lock(current, LockModeType.PESSIMISTIC_WRITE);
		current.setLogins(current.getLogins() + 1);
		entityManager.merge(current);
	}
	
	@Override
	public void newLogout() {
		CommonStatistic current = getCurrent();
		entityManager.lock(current, LockModeType.PESSIMISTIC_WRITE);
		current.setLogouts(current.getLogouts() + 1);
		entityManager.merge(current);
	}
	
	@Override
	public void newMessage() {
		CommonStatistic current = getCurrent();
		entityManager.lock(current, LockModeType.PESSIMISTIC_WRITE);
		current.setMessages(current.getMessages() + 1);
		entityManager.merge(current);
	}

	@Override
	public void startStatisticTimer() {
		LocalDateTime now = LocalDateTime.now();
		if(now.getMinute() > 30) {
			now = now.plusHours(1);
			now = now.withMinute(0);
		} else {
			now = now.withMinute(30);
		}
		now = now.withSecond(0);
		Date offset = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
		
		timerService.createIntervalTimer(0, 1000*60*30 /30/2, new TimerConfig(HALF_HOUR_TIMER_INFO, false));
	}
	
	@Override
	public void stopStatisticTimer() {
		for(Timer t : timerService.getTimers()) {
			if(t.getInfo().equals(HALF_HOUR_TIMER_INFO)) {
				t.cancel();
				break;
			}
		}
	}
	
	@Timeout
	public void halfOurTimerExpried() {
		TypedQuery<CommonStatistic> q = entityManager.createNamedQuery("CommonStatistic.findAllSorted", CommonStatistic.class);
		List<CommonStatistic> l = q.getResultList();
		
		Date fiveMinutesAgo = new Date(System.currentTimeMillis()-5*60*1000);
		
		CommonStatistic halfHourStatistic;
		if(fiveMinutesAgo.before(l.get(0).getStartingDate()) && l.size() > 1)
			halfHourStatistic = l.get(1);
		else
			halfHourStatistic = l.get(0);
			

		broadcast.broadcastMessage(ChatMessage.statistic(halfHourStatistic));
	}
}
