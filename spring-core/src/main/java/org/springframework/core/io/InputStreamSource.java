/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Simple interface for objects that are sources for an {@link InputStream}.
 *
 * 简单的对象接口，这些对象是InputStream的源。
 *
 * <p>This is the base interface for Spring's more extensive {@link Resource} interface.
 *
 * 这是Spring的更具扩展性的Resource接口的基础接口。
 *
 * <p>For single-use streams, {@link InputStreamResource} can be used for any
 * given {@code InputStream}. Spring's {@link ByteArrayResource} or any
 * file-based {@code Resource} implementation can be used as a concrete
 * instance, allowing one to read the underlying content stream multiple times.
 * This makes this interface useful as an abstract content source for mail
 * attachments, for example.
 *
 * 对于一次性流，InputStreamResource可以用于任何给定的InputStream。
 * Spring的ByteArrayResource或任何基于文件的Resource实现都可以作为具体的实例，它允许人们多次读取基础内容数据流。
 * 例如，这使得此接口可用作邮件附件的抽象内容源。
 *
 * @author Juergen Hoeller
 * @since 20.01.2004
 * @see java.io.InputStream
 * @see Resource
 * @see InputStreamResource
 * @see ByteArrayResource
 */
public interface InputStreamSource {

	/**
	 * Return an {@link InputStream} for the content of an underlying resource.
	 *
	 * 返回一个InputStream作为基础资源的内容。
	 *
	 * <p>It is expected that each call creates a <i>fresh</i> stream.
	 *
	 * 它期望每次调用都会创建一个全新的流。
	 *
	 * <p>This requirement is particularly important when you consider an API such
	 * as JavaMail, which needs to be able to read the stream multiple times when
	 * creating mail attachments. For such a use case, it is <i>required</i>
	 * that each {@code getInputStream()} call returns a fresh stream.
	 *
	 * 当您考虑使用像JavaMail这样的API时，这尤其重要，因为当要创建邮件附件时，该API需要能够多次读取数据流。
	 *
	 * @return the input stream for the underlying resource (must not be {@code null})
	 * @throws java.io.FileNotFoundException if the underlying resource doesn't exist
	 * @throws IOException if the content stream could not be opened
	 */
	InputStream getInputStream() throws IOException;

}
