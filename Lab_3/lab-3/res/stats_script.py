"""
Jameson Albers
CS 6500, Spring 2022
Lab 3

This file imports the CSV files created from the response test program and
calculates the mean, median, and 99th percentile latencies for the responses.
Additionally, it plots the latencies over time.
"""
import csv

def get_metrics(filepath):
    with open(filepath) as csv_file:
        pair_list_unsorted = []
        csv_reader = csv.reader(csv_file, delimiter=',')
        print("Reading file " + filepath + ":")
        line_count = 0
        min = float('inf')
        for row in csv_reader:
            if line_count != 0:
                
                pair_list_unsorted.append((int(row[0]),int(row[2])))
                line_count += 1
            else:
                print("Columns: " + row[0] + row[1] + row[2] + row[3])
        print("\nInputs read.")
        print