package com.ruijie.scheduler.job;

import java.util.ArrayList;
import java.util.List;

public class TaskUtil {
    public static <T> List<List<T>> collate(List<T> selfList, int size, int step, boolean keepRemainder) {
        List<List<T>> answer = new ArrayList<>();
        if (size <= 0) {
            answer.add(selfList);
        } else {
            for(int pos = 0; pos < selfList.size() && pos > -1 && (keepRemainder || pos <= selfList.size() - size); pos += step) {
                List<T> element = new ArrayList<>();

                for(int offs = pos; offs < pos + size && offs < selfList.size(); ++offs) {
                    element.add(selfList.get(offs));
                }

                answer.add(element);
            }
        }
        return answer;
    }

}
