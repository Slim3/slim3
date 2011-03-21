/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package coordinate.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container of items.
 * 
 * @author higayasuo
 * @since 1.0.0
 * 
 */
public class Items {

    protected List<Item> topsList = new ArrayList<Item>();

    protected List<Item> bottomsList = new ArrayList<Item>();

    protected List<Item> onepieceList = new ArrayList<Item>();

    public List<Item> getTopsList() {
        return topsList;
    }

    public List<Item> getBottomsList() {
        return bottomsList;
    }

    public List<Item> getOnepieceList() {
        return onepieceList;
    }

    public void add(Item item) {
        switch (item.getCategory()) {
        case TOPS:
            topsList.add(item);
            break;
        case BOTTOMS:
            bottomsList.add(item);
            break;
        case ONEPIECE:
            onepieceList.add(item);
            break;
        default:
            throw new IllegalStateException("Unknown category: "
                + item.getCategory());
        }
    }
}