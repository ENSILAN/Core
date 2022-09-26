package me.nathanfallet.ensilan.core.interfaces;

import java.util.List;

public interface LeaderboardGenerator {

    String getTitle();

    List<String> getLines(int limit);

}
