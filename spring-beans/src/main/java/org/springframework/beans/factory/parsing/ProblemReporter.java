/*
 * Copyright 2002-2012 the original author or authors.
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

/**
 * SPI interface allowing tools and other external processes to handle errors
 * and warnings reported during bean definition parsing.
 *
 * SPI（为某个接口寻找服务实现的机制）接口允许工具和外部处理器处理bean定义解析期间报告的错误和警告信息。
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see Problem
 */
public interface ProblemReporter {

	/**
	 * Called when a fatal error is encountered during the parsing process.
	 * <p>Implementations must treat the given problem as fatal,
	 * i.e. they have to eventually raise an exception.
	 *
	 * 解析处理过程中，当出现致命错误时调用此方法。实现必须把给定的问题（Problem）当做致命问题，即，它们最终不得不抛出异常。
	 *
	 * @param problem the source of the error (never {@code null})
	 */
	void fatal(Problem problem);

	/**
	 * Called when an error is encountered during the parsing process.
	 * <p>Implementations may choose to treat errors as fatal.
	 *
	 * 解析处理过程中，当出现错误时调用。实现可以选择将错误是为致命问题。
	 *
	 * @param problem the source of the error (never {@code null})
	 */
	void error(Problem problem);

	/**
	 * Called when a warning is raised during the parsing process.
	 * <p>Warnings are <strong>never</strong> considered to be fatal.
	 *
	 * 解析处理过程中，当出现警告时调用。永远不要把警告当做致命问题。
	 *
	 * @param problem the source of the warning (never {@code null})
	 */
	void warning(Problem problem);

}
