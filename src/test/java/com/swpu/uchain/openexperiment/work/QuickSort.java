package com.swpu.uchain.openexperiment.work;

public class QuickSort {

    public static void quickSort(int[] arr, int left, int right) {
        if (left >= right){
            return;
        }
        int partition = partition(arr, left, right);
        quickSort(arr, left, partition-1);
        quickSort(arr, partition+1, right);

    }

    private static int partition(int arr[], int left, int right) {
        int value = arr[left];

        int position = left;
        for (int i = left + 1; i <= right; i++) {
            if (arr[i] < value) {
                swap(arr, i, ++position);
            }
        }
        swap(arr, position, left);
        return position;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] a = { 3,1,5,7,2 };
        quickSort(a,  0,a.length-1);
        System.err.println("排好序的数组：");
        for (int e : a) {
            System.out.print(e+" ");
        }
    }

}
