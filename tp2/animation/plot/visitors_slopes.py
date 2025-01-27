import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

from lib.file.csv import get_most_recent_csv

visitors_file = get_most_recent_csv('../../output/visitors-slope-rate/', contains="rates")
df = pd.read_csv(visitors_file)

derivative = df['visited_count'] / df['time']

derivative = derivative.replace([np.inf, -np.inf], np.nan).dropna()

plt.figure(figsize=(10, 6))

plt.plot(df['time'].iloc[:len(derivative)], derivative, linestyle='-')
plt.ylabel('Número de partículas que visitaron / Tiempo (s)')
plt.title(f'N = {df["n"].iloc[0]}, L = {df["l"].iloc[0]}, Eta = 0.2, OBC', fontsize=10, loc='right')
plt.xlabel('Tiempos (s)')
plt.grid(True)
plt.tight_layout()
plt.savefig(f"{df['n'].iloc[0]}_{df['l'].iloc[0]}_0.2_slopes.png")
plt.show()
