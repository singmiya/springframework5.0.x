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

package org.springframework.beans.factory.parsing;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Represents a problem with a bean definition configuration.
 * Mainly serves as common argument passed into a {@link ProblemReporter}.
 *
 * 展示带有bean定义配置的问题对象。主要作为传递到ProblemReporter的通用参数。
 *
 * <p>May indicate a potentially fatal problem (an error) or just a warning.
 *
 * 可以指明一个潜在的致命问题（错误）或仅仅是一个警告。
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see ProblemReporter
 */
public class Problem {

	private final String message;

	private final Location location;

	@Nullable
	private final ParseState parseState;

	@Nullable
	private final Throwable rootCause;


	/**
	 * Create a new instance of the {@link Problem} class.
	 *
	 * 创建一个Problem类的新实例。
	 *
	 * @param message a message detailing the problem 详细说明问题的消息
	 * @param location the location within a bean configuration source that triggered the error bean配置源中触发错误的位置
	 */
	public Problem(String message, Location location) {
		this(message, location, null, null);
	}

	/**
	 * Create a new instance of the {@link Problem} class.
	 *
	 * 创建一个Problem类的新实例。
	 *
	 * @param message a message detailing the problem
	 * @param parseState the {@link ParseState} at the time of the error 发生错误时的ParseState
	 * @param location the location within a bean configuration source that triggered the error
	 */
	public Problem(String message, Location location, ParseState parseState) {
		this(message, location, parseState, null);
	}

	/**
	 * Create a new instance of the {@link Problem} class.
	 *
	 * 创建一个Problem类的新实例。
	 *
	 * @param message a message detailing the problem
	 * @param rootCause the underlying exception that caused the error (may be {@code null}) 导致错误的基础异常
	 * @param parseState the {@link ParseState} at the time of the error
	 * @param location the location within a bean configuration source that triggered the error
	 */
	public Problem(String message, Location location, @Nullable ParseState parseState, @Nullable Throwable rootCause) {
		Assert.notNull(message, "Message must not be null");
		Assert.notNull(location, "Location must not be null");
		this.message = message;
		this.location = location;
		this.parseState = parseState;
		this.rootCause = rootCause;
	}


	/**
	 * Get the message detailing the problem.
	 * 获取详细说明错误的消息
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Get the location within a bean configuration source that triggered the error.
	 * 获取bean配置源中触发错误的位置。
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
	 * Get the description of the bean configuration source that triggered the error,
	 * as contained within this Problem's Location object.
	 * 获取bean配置源中触发错误的位置，包含在Problem的位置对象中。
	 * @see #getLocation()
	 */
	public String getResourceDescription() {
		return getLocation().getResource().getDescription();
	}

	/**
	 * Get the {@link ParseState} at the time of the error (may be {@code null}).
	 * 获取发生错误时的ParseState
	 */
	@Nullable
	public ParseState getParseState() {
		return this.parseState;
	}

	/**
	 * Get the underlying exception that caused the error (may be {@code null}).
	 * 获取导致错误的基础异常。
	 */
	@Nullable
	public Throwable getRootCause() {
		return this.rootCause;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Configuration problem: ");
		sb.append(getMessage());
		sb.append("\nOffending resource: ").append(getResourceDescription());
		if (getParseState() != null) {
			sb.append('\n').append(getParseState());
		}
		return sb.toString();
	}

}
