"""
Jameson Albers
CS 6500, Spring 2022
Lab 3

This file imports the CSV files created from the response test program and
calculates the mean, median, and 99th percentile latencies for the responses.
Additionally, it plots the latencies over time.
"""
import csv
import os
import importlib
try:
    import matplotlib.pyplot as plt
    import numpy as np
except ImportError:
    import subprocess
    import sys
    subprocess.call([sys.executable,'-m','pip','install','matplotlib'])
finally:
    plt = importlib.import_module('matplotlib.pyplot')
    np = importlib.import_module('numpy')
import statistics as stat

def get_time(pair):
    return pair[0]

def ns_to_ms(measurement):
    return measurement / 1000000

def get_metrics(filepath:str):
    bits = ""
    for char in filepath:
        if char.isdigit():
            bits += char
    # Open CSV file
    with open(filepath) as csv_file:
        # List of start time/latency pairs
        pair_list = []
        csv_reader = csv.reader(csv_file)
        print("Reading file " + filepath + ":")
        # Initialize line count to ignore column headers
        line_count = 0
        # Get minimum/maximum
        minimum = float('inf')
        maximum = float('-inf')
        # Append start/latency pairs to list
        for row in csv_reader:
            if line_count != 0: # Non-header lines
                time = int(row[0])
                latency = int(row[2])
                if time < minimum:
                    minimum = time
                if time > maximum:
                    maximum = time
                pair_list.append([time,latency])
                line_count += 1
            else: # Column headers
                line_count += 1
    # Subtract the first start time from each time to get relative start time
    for pair in pair_list:
        pair[0] = pair[0] - minimum
    # Sort list to get pair in 
    pair_list.sort(key=get_time)
    num_buckets = 100
    index_step = pair_list[-1][0] // num_buckets
    bin_list_raw = [[] for x in range(num_buckets)]
    i, j = 0, 0
    offset = 1
    while i < num_buckets:
        bin_list_raw[i] = [pair_list[j][0],[]]
        while pair_list[j][0] < index_step * offset and j < len(pair_list):
            bin_list_raw[i][1].append(pair_list[j][1])
            j += 1
        offset += 1
        i += 1
    time_list = [x[0] for x in bin_list_raw]
    mean_list = [stat.mean(x[1]) for x in bin_list_raw]
    median_list = [stat.median(x[1]) for x in bin_list_raw]
    percentile_99_list = [stat.quantiles(x[1], n=100)[98] for x in bin_list_raw]
    for i in range(len(time_list)):
        time_list[i] = ns_to_ms(time_list[i])
        mean_list[i] = ns_to_ms(mean_list[i])
        median_list[i] = ns_to_ms(median_list[i])
        percentile_99_list[i] = ns_to_ms(percentile_99_list[i])
    
    start_time = np.array(time_list)
    mean = np.array(mean_list)
    median = np.array(median_list)
    percentile_99 = np.array(percentile_99_list)

    plt.plot(start_time, mean, label="Mean")
    plt.plot(start_time, median, label="Median")
    plt.plot(start_time, percentile_99, label="99th Percentile")
    plt.title("Latency Metrics for " + bits + " Thread Test")
    plt.xlabel("Clock Time of Request (ms from Start)")
    plt.ylabel("Latency (ms)")
    plt.legend()
    plt.savefig("metricsplot" + bits + ".png")
    plt.show()
    
abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)

for file in os.listdir():
    if file.startswith("threadlog"):
        get_metrics(file)

print("\nMetric complete.")
