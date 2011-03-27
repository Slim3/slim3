package coordinate.pickrule;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import coordinate.enums.Category;
import coordinate.enums.Length;
import coordinate.enums.Variety;
import coordinate.model.Item;

/**
 * @author higayasuo
 * 
 */
public class OnepieceBottomsTest {

    private OnepieceBottomsRule rule = new OnepieceBottomsRule();

    /**
     * @throws Exception
     */
    @Test
    public void isSecondSkirt() throws Exception {
        Item second = new Item();
        second.setCategory(Category.BOTTOMS);
        second.setVariety(Variety.SKIRT);
        assertThat(rule.isSecondSkirt(second), is(true));
        second.setVariety(Variety.PANTS);
        assertThat(rule.isSecondSkirt(second), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isFirstOnepieceSkirt() throws Exception {
        Item first = new Item();
        first.setCategory(Category.ONEPIECE);
        first.setVariety(Variety.ONEPIECESKIRT);
        assertThat(rule.isFirstOnepieceSkirt(first), is(true));
        first.setVariety(Variety.ALLINONE);
        assertThat(rule.isSecondSkirt(first), is(false));
    }

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
    @Test
    public void isSuitableForOnepieceSkirtAndPants() throws Exception {
        Item first = new Item();
        first.setCategory(Category.ONEPIECE);
        first.setVariety(Variety.ONEPIECESKIRT);
        first.setLength(Length.KNEE);
        Item second = new Item();
        second.setCategory(Category.BOTTOMS);
        second.setVariety(Variety.PANTS);
        second.setLength(Length.BELOWKNEE);
        assertThat(rule.isSuitable(first, second), is(true));
        first.setLength(Length.BELOWKNEE);
        assertThat(rule.isSuitable(first, second), is(false));
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

    /**
     * @throws Exception
     */
    @Test
    public void isOnepieceSkirtShorterThanPants() throws Exception {
        Item first = new Item();
        first.setCategory(Category.ONEPIECE);
        first.setVariety(Variety.ONEPIECESKIRT);
        first.setLength(Length.KNEE);
        Item second = new Item();
        second.setCategory(Category.BOTTOMS);
        second.setVariety(Variety.PANTS);
        second.setLength(Length.BELOWKNEE);
        assertThat(
            rule.isOnepieceSkirtShorterThanPants(first, second),
            is(true));
        first.setLength(Length.BELOWKNEE);
        assertThat(
            rule.isOnepieceSkirtShorterThanPants(first, second),
            is(false));
    }
}
