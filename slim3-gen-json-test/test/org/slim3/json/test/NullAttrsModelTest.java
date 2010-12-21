package org.slim3.json.test;

import junit.framework.Assert;

import org.junit.Test;
import org.slim3.json.test.NullAttrsModel;
import org.slim3.json.test.annotation.NullAttrsModelMeta;

public class NullAttrsModelTest {
	@Test
	public void modelToJson() throws Exception{
		Assert.assertEquals(
				"{\"nullBlob\":null,\"nullBytesBlob\":null,\"nullString\":\"null\"," +
				"\"nullText\":null,\"nullValueText\":null}",
				meta.modelToJson(new NullAttrsModel())
				);
	}
	
	private NullAttrsModelMeta meta = NullAttrsModelMeta.get();
}
