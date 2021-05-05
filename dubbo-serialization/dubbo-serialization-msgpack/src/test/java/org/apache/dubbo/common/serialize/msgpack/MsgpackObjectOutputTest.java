/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.dubbo.common.serialize.msgpack;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class MsgpackObjectOutputTest {
    private MsgpackObjectOutput msgpackObjectOutput;
    private MsgpackObjectInput msgpackObjectInput;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @BeforeEach
    public void setUp() throws Exception {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.msgpackObjectOutput = new MsgpackObjectOutput(byteArrayOutputStream);
    }

    @Test
    public void testWriteBool() throws IOException {
        this.msgpackObjectOutput.writeBool(true);
        this.flushToInput();

        assertThat(msgpackObjectInput.readBool(), is(true));
    }

    @Test
    public void testWriteShort() throws IOException {
        this.msgpackObjectOutput.writeShort((short) 2);
        this.flushToInput();

        assertThat(msgpackObjectInput.readShort(), is((short) 2));
    }

    @Test
    public void testWriteInt() throws IOException {
        this.msgpackObjectOutput.writeInt(1);
        this.flushToInput();

        assertThat(msgpackObjectInput.readInt(), is(1));
    }

    @Test
    public void testWriteLong() throws IOException {
        this.msgpackObjectOutput.writeLong(1000L);
        this.flushToInput();

        assertThat(msgpackObjectInput.readLong(), is(1000L));
    }

    @Test
    public void testWriteUTF() throws IOException {
        this.msgpackObjectOutput.writeUTF("Pace Hasîtî 和平 Мир");
        this.flushToInput();

        assertThat(msgpackObjectInput.readUTF(), is("Pace Hasîtî 和平 Мир"));
    }

    @Test
    public void testWriteUTF2() throws IOException {
        this.msgpackObjectOutput.writeUTF("a");
        this.msgpackObjectOutput.writeUTF("b");
        this.msgpackObjectOutput.writeUTF("b");
        this.flushToInput();
        System.out.println(this.msgpackObjectInput.readUTF());
        System.out.println(this.msgpackObjectInput.readUTF());
        System.out.println(this.msgpackObjectInput.readUTF());
    }


    @Test
    public void testWriteFloat() throws IOException {
        this.msgpackObjectOutput.writeFloat(1.88f);
        this.flushToInput();

        assertThat(this.msgpackObjectInput.readFloat(), is(1.88f));
    }

    @Test
    public void testWriteDouble() throws IOException {
        this.msgpackObjectOutput.writeDouble(1.66d);
        this.flushToInput();

        assertThat(this.msgpackObjectInput.readDouble(), is(1.66d));
    }

    @Test
    public void testWriteBytes() throws IOException {
        this.msgpackObjectOutput.writeBytes("hello".getBytes());
        this.flushToInput();

        assertThat(this.msgpackObjectInput.readBytes(), is("hello".getBytes()));
    }

    @Test
    public void testWriteBytesWithSubLength() throws IOException {
        this.msgpackObjectOutput.writeBytes("hello".getBytes(), 2, 2);
        this.flushToInput();

        assertThat(this.msgpackObjectInput.readBytes(), is("ll".getBytes()));
    }

    @Test
    public void testWriteByte() throws IOException {
        this.msgpackObjectOutput.writeByte((byte) 123);
        this.flushToInput();

        assertThat(this.msgpackObjectInput.readByte(), is((byte) 123));
    }

    @Test
    public void testWriteObject() throws IOException, ClassNotFoundException {
        Image image = new Image("http://dubbo.apache.org/img/dubbo_white.png", "logo", 300, 480, Image.Size.SMALL);
        this.msgpackObjectOutput.writeObject(image);
        this.flushToInput();

        Image readObjectForImage = msgpackObjectInput.readObject(Image.class);
        assertThat(readObjectForImage, not(nullValue()));
        assertThat(readObjectForImage, is(image));
    }

    private void flushToInput() throws IOException {
        this.msgpackObjectOutput.flushBuffer();
        this.byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        this.msgpackObjectInput = new MsgpackObjectInput(byteArrayInputStream);
    }
}
