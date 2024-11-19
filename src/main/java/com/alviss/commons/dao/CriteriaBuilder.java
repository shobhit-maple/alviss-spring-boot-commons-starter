package com.alviss.commons.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class CriteriaBuilder {

    private static final Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(.+?),");

    public static List<SearchCriteria> parseFilters(final String filter) {
        if (StringUtils.isEmpty(filter)) {
            return new ArrayList<>();
        }

        val criteriaList = new ArrayList<SearchCriteria>();
        val matcher = pattern.matcher(filter + ",");
        while (matcher.find()) {
            criteriaList.add(new SearchCriteria(matcher.group(1),
                                                matcher.group(2),
                                                matcher.group(3)));
        }
        return criteriaList;
    }
}
