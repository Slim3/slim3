/*
 * Copyright 2011-2011 Information Services International - Dentsu, Ltd.
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

    /**
     * The list of tops.
     */
    protected List<Item> topsList = new ArrayList<Item>();

    /**
     * The list of bottoms.
     */
    protected List<Item> bottomsList = new ArrayList<Item>();

    /**
     * The list of one-piece.
     */
    protected List<Item> onepieceList = new ArrayList<Item>();

    /**
     * Returns the list of tops.
     * 
     * @return the list of tops
     */
    public List<Item> getTopsList() {
        return topsList;
    }

    /**
     * Returns the list of bottoms.
     * 
     * @return the list of bottoms
     */
    public List<Item> getBottomsList() {
        return bottomsList;
    }

    /**
     * Returns the list of one-piece.
     * 
     * @return the list of one-piece
     */
    public List<Item> getOnepieceList() {
        return onepieceList;
    }

    /**
     * Adds the item.
     * 
     * @param item
     *            the item
     * @throws IllegalStateException
     *             if the item is unexpected
     */
    public void add(Item item) throws IllegalStateException {
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