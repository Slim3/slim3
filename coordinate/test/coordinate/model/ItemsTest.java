package coordinate.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import coordinate.enums.Category;

public class ItemsTest {

    @Test
    public void addForTops() throws Exception {
        Items items = new Items();
        Item item = new Item();
        item.setCategory(Category.TOPS);
        items.add(item);
        assertThat(items.getTopsList().size(), is(1));
        assertThat(items.getBottomsList().size(), is(0));
        assertThat(items.getOnepieceList().size(), is(0));
    }

    @Test
    public void addForBottoms() throws Exception {
        Items items = new Items();
        Item item = new Item();
        item.setCategory(Category.BOTTOMS);
        items.add(item);
        assertThat(items.getTopsList().size(), is(0));
        assertThat(items.getBottomsList().size(), is(1));
        assertThat(items.getOnepieceList().size(), is(0));
    }

    @Test
    public void addForOnepiece() throws Exception {
        Items items = new Items();
        Item item = new Item();
        item.setCategory(Category.ONEPIECE);
        items.add(item);
        assertThat(items.getTopsList().size(), is(0));
        assertThat(items.getBottomsList().size(), is(0));
        assertThat(items.getOnepieceList().size(), is(1));
    }
}
