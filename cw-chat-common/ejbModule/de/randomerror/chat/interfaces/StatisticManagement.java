package de.randomerror.chat.interfaces;

import java.util.List;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;

public interface StatisticManagement {
	List<CommonStatistic> getStatistics();
}
