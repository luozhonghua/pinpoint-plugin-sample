/**
 * Copyright 2014 NAVER Corp.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navercorp.pinpoint.plugin.example;

import static com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier.ExpectedAnnotation.*;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier.BlockType;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier.ExpectedAnnotation;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.target.TargetClass2;

/**
 * @see {@link MyPlugin#example2_Inject_Custom_Interceptor(com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginContext)}
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent("target/my-pinpoint-agent")
@Dependency({"com.navercorp.pinpoint:plugin-example-target:[1.0.0,)"})
public class Example2_Inject_Custom_Interceptor_IT {

    @Test
    public void test() throws Exception {
        String name = "Pinpoint";

        TargetClass2 target = new TargetClass2();
        String hello = target.targetMethod(name);
        
        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache(System.out);
        verifier.printBlocks(System.out);
        
        Method targetMethod = TargetClass2.class.getMethod("targetMethod", String.class);
        
        ExpectedAnnotation[] args = args(name);
        ExpectedAnnotation returnValue = annotation("ReturnValue", hello);
        
        verifier.verifyTraceBlock(BlockType.EVENT, "PluginExample", targetMethod, null, null, null, null, args[0], returnValue);
        
        // no more traces
        verifier.verifyTraceBlockCount(0);
    }
}