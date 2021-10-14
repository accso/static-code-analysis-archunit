package de.accso.library.statistics;

import de.accso.library.datamanagement.manager.LoanDao;
import de.accso.library.datamanagement.model.Loan;
import de.accso.library.datamanagement.model.MediaType;
import de.accso.library.statistics.resulttype.MediaTypeWithCount;
import de.accso.library.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class encapsulates statistics regarding {@link MediaType}s.
 */
@Component
public class MediaTypeStatistics {

	@Autowired
	private LoanDao loanDao;

	/**
	 * Determines for each {@link MediaType}, <code>mt</code>, how many loans were
	 * made in the given <code>year</code> for <code>mt</code>.
	 */
	public List<MediaTypeWithCount> determineMediaTypesByLoansForYear(int year) {
		DateTimeUtils dateTimeUtils = DateTimeUtils.getInstance();
		LocalDateTime startTimeForYear = LocalDateTime.of(year, 1, 1, 0, 0, 0);
		LocalDateTime endTimeForYear = LocalDateTime.of(year, 12, 31, 23, 59, 59);

		Map<MediaType, Long> countsPerMediaType = initializeCountsPerMediaType();
		List<Loan> allLoansInGivenYear = determineAllLoansForYear(dateTimeUtils, startTimeForYear, endTimeForYear);
		allLoansInGivenYear.forEach(loan -> {
			loan.getMediaInstancesInLoan().forEach(borrowedMedium -> {
				MediaType mediaTypeOfBorrowedMedium = MediaType.getMediaTypeFor(borrowedMedium.getMedia());
				long incrementedCount = countsPerMediaType.get(mediaTypeOfBorrowedMedium) + 1;
				countsPerMediaType.put(mediaTypeOfBorrowedMedium, incrementedCount);
			});
		});

		List<MediaTypeWithCount> mediaTypesWithCounts = transformCountsPerMediaTypeMapToList(countsPerMediaType);
		sortMediaTypesWithCountsByCountDesc(mediaTypesWithCounts);
		return mediaTypesWithCounts;
	}

	private List<Loan> determineAllLoansForYear(DateTimeUtils dateTimeUtils, LocalDateTime startTimeForYear,
			LocalDateTime endTimeForYear) {
		// @formatter:off
		return loanDao.findAll().stream().filter(l ->
			dateTimeUtils.isDateTimeBetween(l.getLoanDate(), startTimeForYear, endTimeForYear)
		).collect(Collectors.toList());
		// @formatter:on
	}

	private Map<MediaType, Long> initializeCountsPerMediaType() {
		Map<MediaType, Long> initializedMap = new HashMap<>();
		for (MediaType mediaType : MediaType.values()) {
			initializedMap.put(mediaType, 0L);
		}
		return initializedMap;
	}

	private List<MediaTypeWithCount> transformCountsPerMediaTypeMapToList(Map<MediaType, Long> map) {
		List<MediaTypeWithCount> transformedList = new ArrayList<>();
		// @formatter:off
		map.entrySet().stream().forEach(entry ->
			transformedList.add(new MediaTypeWithCount(entry.getKey(), entry.getValue()))
		);
		// @formatter:on
		return transformedList;
	}

	/**
	 * Takes the given <code>listToSort</code> and sorts this list in descending
	 * order by their {@link MediaTypeWithCount#getCount()} values. The sorting is
	 * done <b>in-place</b>.
	 */
	private void sortMediaTypesWithCountsByCountDesc(List<MediaTypeWithCount> listToSort) {
		listToSort.sort((item1, item2) -> Long.compare(item2.getCount(), item1.getCount()));
	}

}
