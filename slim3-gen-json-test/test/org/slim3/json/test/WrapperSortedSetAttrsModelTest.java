package org.slim3.json.test;

import java.util.Arrays;
import java.util.TreeSet;

import net.arnx.jsonic.JSON;

import org.junit.Assert;
import org.junit.Test;

public class WrapperSortedSetAttrsModelTest {
    @Test
    public void modelToJson() {
        WrapperSortedSetAttrsModelMeta m = WrapperSortedSetAttrsModelMeta.get();
        WrapperSortedSetAttrsModel model = new WrapperSortedSetAttrsModel();
        model.setBooleanSortedSetAttr(new TreeSet<Boolean>(Arrays.asList(
            true,
            false,
            true)));
        model.setShortSortedSetAttr(new TreeSet<Short>(Arrays.asList(
            (short) 9,
            (short) 8,
            (short) 7)));
        model.setIntegerSortedSetAttr(new TreeSet<Integer>(Arrays.asList(
            9,
            8,
            7)));
        model
            .setLongSortedSetAttr(new TreeSet<Long>(Arrays.asList(9L, 8L, 7L)));
        model.setFloatSortedSetAttr(new TreeSet<Float>(Arrays.asList(
            9.9f,
            8.8f,
            7.7f)));
        model.setDoubleSortedSetAttr(new TreeSet<Double>(Arrays.asList(
            9.9,
            8.8,
            7.7)));

        String json = m.modelToJson(model);
        System.out.println(json);
        JSON j = new JSON();
        j.setSuppressNull(true);
        System.out.println(j.format(model));

        Assert
            .assertEquals(
                "{\"booleanSortedSetAttr\":[false,true],\"doubleSortedSetAttr\":[7.7,8.8,9.9]"
                    + ",\"floatSortedSetAttr\":[7.7,8.8,9.9],\"integerSortedSetAttr\":[7,8,9]"
                    + ",\"longSortedSetAttr\":[7,8,9],\"shortSortedSetAttr\":[7,8,9]}",
                json);
    }

    @Test
    public void modelToJson_null() throws Exception {
        WrapperSortedSetAttrsModel m = new WrapperSortedSetAttrsModel();
        String json = WrapperSortedSetAttrsModelMeta.get().modelToJson(m);
        Assert.assertEquals("{}", json);
    }

    @Test
    public void jsonToModel() throws Exception {
        WrapperSortedSetAttrsModel m =
            WrapperSortedSetAttrsModelMeta.get().jsonToModel(
                "{\"booleanSortedSetAttr\":[true,false,true]"
                    + ",\"doubleSortedSetAttr\":[9.9,8.8,7.7]"
                    + ",\"floatSortedSetAttr\":[9.9,8.8,7.7]"
                    + ",\"integerSortedSetAttr\":[9,8,7]"
                    + ",\"longSortedSetAttr\":[9,8,7]"
                    + ",\"shortSortedSetAttr\":[9,8,7]}");
        Assert.assertArrayEquals(
            new TreeSet<Boolean>(Arrays.asList(true, false, true)).toArray(),
            m.getBooleanSortedSetAttr().toArray());
        Assert.assertArrayEquals(
            new TreeSet<Double>(Arrays.asList(9.9, 8.8, 7.7)).toArray(),
            m.getDoubleSortedSetAttr().toArray());
        Assert.assertArrayEquals(
            new TreeSet<Float>(Arrays.asList(9.9f, 8.8f, 7.7f)).toArray(),
            m.getFloatSortedSetAttr().toArray());
        Assert.assertArrayEquals(
            new TreeSet<Integer>(Arrays.asList(9, 8, 7)).toArray(),
            m.getIntegerSortedSetAttr().toArray());
        Assert.assertArrayEquals(
            new TreeSet<Long>(Arrays.asList(9L, 8L, 7L)).toArray(),
            m.getLongSortedSetAttr().toArray());
        Assert.assertArrayEquals(
            new TreeSet<Short>(Arrays.asList((short) 9, (short) 8, (short) 7))
                .toArray(),
            m.getShortSortedSetAttr().toArray());
    }
}
