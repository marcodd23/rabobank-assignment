package io.sytac.rabobank.app.utils;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.io.Reader;

@RunWith(MockitoJUnitRunner.class)
public class FileUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getReaderTest() {
        Reader reader = FileUtil.getReader("records.csv");
        Assertions.assertThat(reader).isNotNull();

    }
}