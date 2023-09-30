package org.nefcup.client.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IgnoreService {
    private final Set<String> patternSet;

    public IgnoreService(String patternsText) {
        this.patternSet = new HashSet<>(parsePatterns(patternsText));
        this.patternSet.add("ignore.nefcup");
        this.patternSet.add("nefcup.sh");
    }

    public static List<String> parsePatterns(String patternsText) {
        if (patternsText==null){
            return new ArrayList<>();
        }
        return patternsText.lines()
                .filter(it -> !it.isBlank())
                .filter(it -> !it.startsWith("#"))
                .map(it -> it.replace("\\#","#"))
                .collect(Collectors.toList());
    }

    public boolean isIgnore(String fileName){
        return patternSet.contains(fileName);
    }


}
