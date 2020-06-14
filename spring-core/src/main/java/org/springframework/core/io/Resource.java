/*
 * Copyright 2002-2018 the original author or authors.
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.springframework.lang.Nullable;

/**
 * Interface for a resource descriptor that abstracts from the actual
 * type of underlying resource, such as a file or class path resource.
 *
 * 从基础资源的实际类型中抽象出来的资源描述符接口，例如：文件或者类路径（classpath）资源
 *
 * <p>An InputStream can be opened for every resource if it exists in
 * physical form, but a URL or File handle can just be returned for
 * certain resources. The actual behavior is implementation-specific.
 *
 * 对于每一个以物理形式存在的资源都可以打开一个InputStream，但是对于某些特定资源仅仅返回一个URL或者文件句柄。其具体表现有其实现指定。
 *
 * @author Juergen Hoeller
 * @since 28.12.2003
 * @see #getInputStream()
 * @see #getURL()
 * @see #getURI()
 * @see #getFile()
 * @see WritableResource
 * @see ContextResource
 * @see UrlResource
 * @see FileUrlResource
 * @see FileSystemResource
 * @see ClassPathResource
 * @see ByteArrayResource
 * @see InputStreamResource
 */
public interface Resource extends InputStreamSource {

	/**
	 * Determine whether this resource actually exists in physical form.
	 * <p>This method performs a definitive existence check, whereas the
	 * existence of a {@code Resource} handle only guarantees a valid
	 * descriptor handle.
	 *
	 * 判断资源是否以物理形式存在。这个方法执行确定的存在性检查，而存在的Resource句柄只保证描述符句柄有效。
	 *
	 */
	boolean exists();

	/**
	 * Indicate whether the contents of this resource can be read via
	 * {@link #getInputStream()}.
	 * <p>Will be {@code true} for typical resource descriptors;
	 * note that actual content reading may still fail when attempted.
	 * However, a value of {@code false} is a definitive indication
	 * that the resource content cannot be read.
	 * @see #getInputStream()
	 *
	 * 指明资源中的内容是否可以通过getInputStream()方法读取。对于典型的资源描述符其返回值为true；
	 * 请注意，尝试对实际内容读取时仍可能会失败。然而，如果返回值为 false，就表明资源内容不可读。
	 *
	 */
	default boolean isReadable() {
		return true;
	}

	/**
	 * Indicate whether this resource represents a handle with an open stream.
	 * If {@code true}, the InputStream cannot be read multiple times,
	 * and must be read and closed to avoid resource leaks.
	 * <p>Will be {@code false} for typical resource descriptors.
	 *
	 * 指明资源是否代表一个打开字节流的句柄。true，InputStream无法多次读取，并且读取后必须关闭以避免资源泄露。
	 * 对于典型的资源描述符其返回值为false
	 *
	 */
	default boolean isOpen() {
		return false;
	}

	/**
	 * Determine whether this resource represents a file in a file system.
	 * A value of {@code true} strongly suggests (but does not guarantee)
	 * that a {@link #getFile()} call will succeed.
	 * <p>This is conservatively {@code false} by default.
	 *
	 * 指明资源是否代表文件系统中的一个文件。强烈建议其返回值为true（但不保证），这样调用getFile()方法就会成功。
	 * 默认情况下，就会返回保守值--false
	 *
	 * @since 5.0
	 * @see #getFile()
	 */
	default boolean isFile() {
		return false;
	}

	/**
	 * Return a URL handle for this resource.
	 *
	 * 返回此资源的URL句柄。
	 *
	 * @throws IOException if the resource cannot be resolved as URL,
	 * i.e. if the resource is not available as descriptor
	 * 如果资源无法解析为URL抛出异常，
	 * 即：如果资源不可用作描述符
	 */
	URL getURL() throws IOException;

	/**
	 * Return a URI handle for this resource.
	 *
	 * 返回此资源的URI句柄
	 *
	 * @throws IOException if the resource cannot be resolved as URI,
	 * i.e. if the resource is not available as descriptor
	 * 如果资源无法解析为URI，
	 * 即：如果资源不可用作描述符
	 * @since 2.5
	 */
	URI getURI() throws IOException;

	/**
	 * Return a File handle for this resource.
	 *
	 * 返回此资源的文件（File）句柄。
	 *
	 * @throws java.io.FileNotFoundException if the resource cannot be resolved as
	 * absolute file path, i.e. if the resource is not available in a file system
	 *
	 * 如果资源无法解析为相对文件路径，即：资源在文件系统中不可用。
	 *
	 * @throws IOException in case of general resolution/reading failures 如果出现一般解析/读取失败
	 * @see #getInputStream()
	 */
	File getFile() throws IOException;

	/**
	 * Return a {@link ReadableByteChannel}.
	 * <p>It is expected that each call creates a <i>fresh</i> channel.
	 * <p>The default implementation returns {@link Channels#newChannel(InputStream)}
	 * with the result of {@link #getInputStream()}.
	 *
	 * 返回一个ReadableByteChannel。它期望每一次调用都会创建一个新的channel。
	 * 默认返回Channels#newChannel(InputStream)，其参数为getInputStream()方法调用返回结果。
	 *
	 * @return the byte channel for the underlying resource (must not be {@code null})
	 * 基础资源的字节通道（不能为null）
	 * @throws java.io.FileNotFoundException if the underlying resource doesn't exist 如果基础资源不存在
	 * @throws IOException if the content channel could not be opened 如果内容通道无法打开
	 * @since 5.0
	 * @see #getInputStream()
	 */
	default ReadableByteChannel readableChannel() throws IOException {
		return Channels.newChannel(getInputStream());
	}

	/**
	 * Determine the content length for this resource.
	 *
	 * 确定此资源的长度。
	 *
	 * @throws IOException if the resource cannot be resolved 如果资源无法解析
	 * (in the file system or as some other known physical resource type) 在文件系统中或一些其他已知的物理资源类型
	 */
	long contentLength() throws IOException;

	/**
	 * Determine the last-modified timestamp for this resource.
	 *
	 * 确定此资源最后修改时间戳。
	 *
	 * @throws IOException if the resource cannot be resolved 如果资源无法解析
	 * (in the file system or as some other known physical resource type) 在文件系统中或一些其他已知的物理资源类型
	 */
	long lastModified() throws IOException;

	/**
	 * Create a resource relative to this resource.
	 *
	 * 相对于此资源（路径）创建资源。
	 *
	 * @param relativePath the relative path (relative to this resource) 相对路径（相对于此资源）
	 * @return the resource handle for the relative resource 相对资源的资源句柄
	 * @throws IOException if the relative resource cannot be determined 如果无法确定相对资源
	 */
	Resource createRelative(String relativePath) throws IOException;

	/**
	 * Determine a filename for this resource, i.e. typically the last
	 * part of the path: for example, "myfile.txt".
	 * <p>Returns {@code null} if this type of resource does not
	 * have a filename.
	 *
	 * 确定此资源的文件名称，即：通常是路径的最后一部分：例如，"myfile.txt"。
	 * 如果这个类型的资源没有文件名，则返回null。
	 *
	 */
	@Nullable
	String getFilename();

	/**
	 * Return a description for this resource,
	 * to be used for error output when working with the resource.
	 * <p>Implementations are also encouraged to return this value
	 * from their {@code toString} method.
	 *
	 * 返回此资源的描述。当使用此资源时用来输出错误。鼓励其实现用它们的toString()方法返回该值。
	 *
	 * @see Object#toString()
	 */
	String getDescription();

}
