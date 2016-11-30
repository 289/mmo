package mmo.line;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Shuai
 */
public class SubLineMgr {
    public static final List<SubLine> subLineList = new ArrayList<>();

    public static void update(){
        subLineList.forEach(subLine -> subLine.update());
    }
}
