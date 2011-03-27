package coordinate.rule;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import coordinate.enums.Category;
import coordinate.enums.Variety;
import coordinate.model.Item;
import coordinate.pickrule.OnepieceBottomsRule;
import coordinate.pickrule.Rule;

/**
 * @author higayasuo
 * 
 */
public class OnepieceBottomsTest {

    private Rule rule = new OnepieceBottomsRule();

    /**
     * @throws Exception
     */
    @Test
    public void isSuitableForSkirt() throws Exception {
        Item first = new Item();
        first.setCategory(Category.ONEPIECE);
        Item second = new Item();
        second.setCategory(Category.BOTTOMS);
        second.setVariety(Variety.SKIRT);
        assertThat(rule.isSuitable(first, second), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isSuitableForPants() throws Exception {
        Item first = new Item();
        first.setCategory(Category.ONEPIECE);
        Item second = new Item();
        second.setCategory(Category.BOTTOMS);
        second.setVariety(Variety.PANTS);
        assertThat(rule.isSuitable(first, second), is(true));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void firstIsTops() throws Exception {
        Item first = new Item();
        first.setCategory(Category.TOPS);
        Item second = new Item();
        second.setCategory(Category.BOTTOMS);
        rule.isSuitable(first, second);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void secondIsTops() throws Exception {
        Item first = new Item();
        first.setCategory(Category.ONEPIECE);
        Item second = new Item();
        second.setCategory(Category.TOPS);
        rule.isSuitable(first, second);
    }
}
