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
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.randomerror.chat.interfaces.BroadcastingLocal;
import de.randomerror.chat.interfaces.StatisticManagementLocal;
import de.randomerror.chat.interfaces.StatisticManagementRemote;

@Singleton
public class StatisticManagementBean implements StatisticManagementLocal, StatisticManagementRemote {

	private List<CommonStatistic> statistics = new LinkedList<>();
	private CommonStatistic current;
	
	@Inject
	private BroadcastingLocal broadcast;
	
	@Resource
	private TimerService timerService;
	
	private Timer halfHourTimer;
	private CommonStatistic halfHourStatistic = new CommonStatistic();
	
	@PostConstruct
	public void init() {
		current = new CommonStatistic();
		current.setStartingDate(new Date());
	}
	
	@Override
	public List<CommonStatistic> getStatistics() {
		return statistics;
	}
	
	@Schedule(hour="*")
	public void newCommonStatistic(Timer timer) {
		current.setEndDate(new Date());
		statistics.add(current);
		current = new CommonStatistic();
		current.setStartingDate(new Date());
	}
	
	@Override
	public void newLogin() {
		current.setLogins(current.getLogins() + 1);
		halfHourStatistic.setLogins(halfHourStatistic.getLogins() + 1);
	}
	
	@Override
	public void newLogout() {
		current.setLogouts(current.getLogouts() + 1);
		halfHourStatistic.setLogouts(halfHourStatistic.getLogouts() + 1);
	}
	
	@Override
	public void newMessage() {
		current.setMessages(current.getMessages() + 1);
		halfHourStatistic.setMessages(halfHourStatistic.getMessages() + 1);
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
		
		halfHourTimer = timerService.createIntervalTimer(offset, 1000*60*30, new TimerConfig());
	}
	
	@Override
	public void stopStatisticTimer() {
		halfHourTimer.cancel();
	}
	
	@Timeout
	public void halfOurTimerExpried() {
		broadcast.broadcastMessage(ChatMessage.statistic(halfHourStatistic));
		
		halfHourStatistic = new CommonStatistic();
	}
}
