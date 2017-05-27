/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yang.parallelsort;

/**
 *
 * @author Richard Yang
 */

import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class MainParallelsort {
    
    private static int N = 10000;
 
// distributing the effort
    private static int searchKey = 100;
    private static int[] arrayToSort;
    
   public static void main (String ... args) {
       
       MainParallelsort ms = new MainParallelsort();
       ms.start();
          
   }
   
   private void start() {
       
        arrayToSort = new int[N];
//fill the array with random numbers
        for (int i = 0; i < N; i++) {
            arrayToSort[i] = ThreadLocalRandom.current().nextInt(0, 1000);
        }
        
        System.out.println("Before sorting ----------------");
        printData(arrayToSort);
        
        Arrays.parallelSort(arrayToSort);
        
        System.out.println("After sorting ------------------");
        printData(arrayToSort);
          
   }
    
   private void printData(int[] arrayToSort) {
       
       for (int index = 0; index < arrayToSort.length; index++) 
           System.out.println("arrayToSort[" + index + "] = " + arrayToSort[index]);
       
   } 
   
   
}
