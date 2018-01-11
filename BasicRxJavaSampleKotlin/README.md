Room & RxJava (Kotlin) Sample
============================

The code in this project is a fork of
the Android Room Kotlin example at repo https://github.com/googlesamples/android-architecture-components

This project exemplifies a compile error while using Android Room.  Specifically, Room does not support
a Single Boolean return from DAO.  The current workaround is to assume that booleans are stored as integers
and to then assume that 0 == false and 1 == true.  In this way, we can instead create a function in the DAO
to return a Single of type integer and then manually map it to boolean.

There is one commit unique to this fork.  It creates a boolean `isActive` field in User, attempts to
provides a Single<Boolean> method in the UserDao to return the value of `isActive`, and adds tests
for the new DAO functionality. The commit also updates the readme.

The project fails to compile with the following compiler stack trace:

```
Error:java.lang.IllegalStateException: failed to analyze: java.util.NoSuchElementException: List is empty.
	at org.jetbrains.kotlin.analyzer.AnalysisResult.throwIfError(AnalysisResult.kt:57)
	at org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler.compileModules(KotlinToJVMBytecodeCompiler.kt:138)
	at org.jetbrains.kotlin.cli.jvm.K2JVMCompiler.doExecute(K2JVMCompiler.kt:170)
	at org.jetbrains.kotlin.cli.jvm.K2JVMCompiler.doExecute(K2JVMCompiler.kt:58)
	at org.jetbrains.kotlin.cli.common.CLICompiler.execImpl(CLICompiler.java:93)
	at org.jetbrains.kotlin.cli.common.CLICompiler.execImpl(CLICompiler.java:46)
	at org.jetbrains.kotlin.cli.common.CLITool.exec(CLITool.kt:92)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl$compile$1$2.invoke(CompileServiceImpl.kt:386)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl$compile$1$2.invoke(CompileServiceImpl.kt:98)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl$doCompile$$inlined$ifAlive$lambda$2.invoke(CompileServiceImpl.kt:832)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl$doCompile$$inlined$ifAlive$lambda$2.invoke(CompileServiceImpl.kt:98)
	at org.jetbrains.kotlin.daemon.common.DummyProfiler.withMeasure(PerfUtils.kt:137)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl.checkedCompile(CompileServiceImpl.kt:859)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl.doCompile(CompileServiceImpl.kt:831)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl.compile(CompileServiceImpl.kt:385)
	at sun.reflect.GeneratedMethodAccessor80.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at sun.rmi.server.UnicastServerRef.dispatch(UnicastServerRef.java:346)
	at sun.rmi.transport.Transport$1.run(Transport.java:200)
	at sun.rmi.transport.Transport$1.run(Transport.java:197)
	at java.security.AccessController.doPrivileged(Native Method)
	at sun.rmi.transport.Transport.serviceCall(Transport.java:196)
	at sun.rmi.transport.tcp.TCPTransport.handleMessages(TCPTransport.java:568)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.run0(TCPTransport.java:826)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.lambda$run$0(TCPTransport.java:683)
	at java.security.AccessController.doPrivileged(Native Method)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.run(TCPTransport.java:682)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at java.lang.Thread.run(Thread.java:745)
Caused by: java.util.NoSuchElementException: List is empty.
	at kotlin.collections.CollectionsKt___CollectionsKt.first(_Collections.kt:178)
	at android.arch.persistence.room.solver.TypeAdapterStore.findColumnTypeAdapter(TypeAdapterStore.kt:235)
	at android.arch.persistence.room.solver.TypeAdapterStore.findCursorValueReader(TypeAdapterStore.kt:183)
	at android.arch.persistence.room.solver.TypeAdapterStore.findRowAdapter(TypeAdapterStore.kt:341)
	at android.arch.persistence.room.solver.TypeAdapterStore.findQueryResultAdapter(TypeAdapterStore.kt:270)
	at android.arch.persistence.room.solver.binderprovider.RxCallableQueryResultBinderProvider.provide(RxCallableQueryResultBinderProvider.kt:40)
	at android.arch.persistence.room.solver.TypeAdapterStore.findQueryResultBinder(TypeAdapterStore.kt:256)
	at android.arch.persistence.room.processor.QueryMethodProcessor.process(QueryMethodProcessor.kt:89)
	at android.arch.persistence.room.processor.DaoProcessor.process(DaoProcessor.kt:89)
	at android.arch.persistence.room.processor.DatabaseProcessor.doProcess(DatabaseProcessor.kt:94)
	at android.arch.persistence.room.processor.DatabaseProcessor.process(DatabaseProcessor.kt:54)
	at android.arch.persistence.room.RoomProcessor$DatabaseProcessingStep.process(RoomProcessor.kt:54)
	at com.google.auto.common.BasicAnnotationProcessor.process(BasicAnnotationProcessor.java:318)
	at com.google.auto.common.BasicAnnotationProcessor.process(BasicAnnotationProcessor.java:171)
	at com.sun.tools.javac.processing.JavacProcessingEnvironment.callProcessor(JavacProcessingEnvironment.java:794)
	at com.sun.tools.javac.processing.JavacProcessingEnvironment.discoverAndRunProcs(JavacProcessingEnvironment.java:705)
	at com.sun.tools.javac.processing.JavacProcessingEnvironment.access$1800(JavacProcessingEnvironment.java:91)
	at com.sun.tools.javac.processing.JavacProcessingEnvironment$Round.run(JavacProcessingEnvironment.java:1035)
	at com.sun.tools.javac.processing.JavacProcessingEnvironment.doProcessing(JavacProcessingEnvironment.java:1176)
	at com.sun.tools.javac.main.JavaCompiler.processAnnotations(JavaCompiler.java:1170)
	at com.sun.tools.javac.main.JavaCompiler.processAnnotations(JavaCompiler.java:1068)
	at org.jetbrains.kotlin.kapt3.AnnotationProcessingKt.doAnnotationProcessing(annotationProcessing.kt:73)
	at org.jetbrains.kotlin.kapt3.AnnotationProcessingKt.doAnnotationProcessing$default(annotationProcessing.kt:42)
	at org.jetbrains.kotlin.kapt3.AbstractKapt3Extension.runAnnotationProcessing(Kapt3Extension.kt:205)
	at org.jetbrains.kotlin.kapt3.AbstractKapt3Extension.analysisCompleted(Kapt3Extension.kt:166)
	at org.jetbrains.kotlin.kapt3.ClasspathBasedKapt3Extension.analysisCompleted(Kapt3Extension.kt:82)
	at org.jetbrains.kotlin.cli.jvm.compiler.TopDownAnalyzerFacadeForJVM$analyzeFilesWithJavaIntegration$2.invoke(TopDownAnalyzerFacadeForJVM.kt:96)
	at org.jetbrains.kotlin.cli.jvm.compiler.TopDownAnalyzerFacadeForJVM.analyzeFilesWithJavaIntegration(TopDownAnalyzerFacadeForJVM.kt:106)
	at org.jetbrains.kotlin.cli.jvm.compiler.TopDownAnalyzerFacadeForJVM.analyzeFilesWithJavaIntegration$default(TopDownAnalyzerFacadeForJVM.kt:83)
	at org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler$analyze$1.invoke(KotlinToJVMBytecodeCompiler.kt:377)
	at org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler$analyze$1.invoke(KotlinToJVMBytecodeCompiler.kt:68)
	at org.jetbrains.kotlin.cli.common.messages.AnalyzerWithCompilerReport.analyzeAndReport(AnalyzerWithCompilerReport.kt:96)
	at org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler.analyze(KotlinToJVMBytecodeCompiler.kt:368)
	at org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler.compileModules(KotlinToJVMBytecodeCompiler.kt:133)
	... 29 more
```


License
--------

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
