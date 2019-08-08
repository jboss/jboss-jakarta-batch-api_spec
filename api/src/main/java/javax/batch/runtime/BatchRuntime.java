/*
 * Copyright 2012, 2019 International Business Machines Corp.
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package javax.batch.runtime;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ServiceLoader;
import javax.batch.operations.BatchRuntimeException;
import javax.batch.operations.JobOperator;

/**
 * BatchRuntime represents the Jakarta Batch Runtime.
 * It provides factory access to the JobOperator interface.
 *
 */
public class BatchRuntime {
	/**
	 * The getJobOperator factory method returns
	 * an instance of the JobOperator interface.
	 *
	 * @return JobOperator instance.
	 */
	public static JobOperator getJobOperator() {
		JobOperator jobOperator = null;
		if (System.getSecurityManager() == null) {
			for(JobOperator op : ServiceLoader.load(JobOperator.class)) {
				jobOperator = op;
				break;
			}
		} else {
			jobOperator = AccessController.doPrivileged(new PrivilegedAction<JobOperator>() {
				public JobOperator run() {
					for (JobOperator op : ServiceLoader.load(JobOperator.class)) {
						return op;
					}
					return null;
				}
			});
		}

		if (jobOperator == null) {
			throw new BatchRuntimeException("The ServiceLoader was unable to find an implementation for JobOperator. Check classpath for META-INF/services/javax.batch.operations.JobOperator file.");
		}
		return jobOperator;
	}
}
