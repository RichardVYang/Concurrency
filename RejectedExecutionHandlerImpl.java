/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yang.concurrentcollection.threadpoolexecutor2;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author Richard Yang
 */
public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {
 
    @Override 
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) { 
        System.out.println(r.toString() + " is rejected"); 
 } 

}
