package com.soniccandle.model;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import org.junit.*;

public class XuggleTest {

    @Test
    public void testMediaReader(){
        IMediaReader mediaReader = ToolFactory.makeReader("Blank Path");
        assert mediaReader != null;
    }
}
