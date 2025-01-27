import argparse
from typing import Sequence

import cv2
import numpy as np
import pandas as pd
from pandas import DataFrame

from lib.video.video_builder import VideoBuilder

video_name = "visitors.mp4"
video_width = 800
video_height = 800
visiting_area_color: Sequence[float] = (50, 50, 50)

default_color = (100, 100, 100)
has_visited_color = (0, 0, 255)
is_visiting_color = (223.78, 49.086, 48.442)
triangle_length = 8
triangle_base_ratio = 2.5

vector_color = (255, 255, 255)
vector_length = 15


def draw_visitors(video_builder: VideoBuilder, data: DataFrame):
    grid_size = data['l'].iloc[0]

    # Render visiting area

    center_x = int((data['visiting_area_x'].iloc[0] / grid_size) * video_width)
    center_y = int((data['visiting_area_y'].iloc[0] / grid_size) * video_height)
    radius = int((data['visiting_area_radius'].iloc[0] / grid_size) * video_width)

    video_builder.draw_frame(lambda frame: cv2.circle(frame, (center_x, center_y), radius, visiting_area_color, -1))

    for index, row in data.iterrows():
        x, y = int((row['x'] / grid_size) * video_width), int(
            (row['y'] / grid_size) * video_height)

        # Render particles

        angle = float(row['angle'])
        color = default_color

        has_visited = bool(row['has_visited'])
        is_visiting = bool(row['is_visiting'])
        if has_visited: color = has_visited_color
        if is_visiting: color = is_visiting_color

        # Render line to represent the movement direction
        x2 = x + int(vector_length * np.cos(angle))
        y2 = y + int(vector_length * np.sin(angle))

        video_builder.draw_frame(lambda frame: cv2.line(frame, (x, y), (x2, y2), vector_color, 1))

        # Render a triangle pointing in the movement direction
        tip_x = x + int(triangle_length * np.cos(angle))
        tip_y = y + int(triangle_length * np.sin(angle))

        base_left_x = x + int(triangle_length * np.cos(angle + np.pi * 2 / triangle_base_ratio))
        base_left_y = y + int(triangle_length * np.sin(angle + np.pi * 2 / triangle_base_ratio))

        base_right_x = x + int(triangle_length * np.cos(angle - np.pi * 2 / triangle_base_ratio))
        base_right_y = y + int(triangle_length * np.sin(angle - np.pi * 2 / triangle_base_ratio))

        triangle_pts = np.array([[tip_x, tip_y], [base_left_x, base_left_y], [base_right_x, base_right_y]], np.int32)
        triangle_pts = triangle_pts.reshape((-1, 1, 2))

        video_builder.draw_frame(lambda frame: cv2.fillPoly(frame, [triangle_pts], color))


def draw_stats(video_builder: VideoBuilder, data: DataFrame):
    timestep = data['time'].values[0]
    visiting_count = data['visiting_count'].values[0]
    visited_count = data['visited_count'].values[0]

    text = f"TIMESTEP: {timestep}, VISITING: {visiting_count}, VISITED: {visited_count}"

    video_builder.draw_frame(lambda frame:
                             cv2.putText(video_builder.current_frame, text,
                                         (20, 40), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1))


def render_visitors(show_stats: bool):
    visitors_file = '../../output/visitors/visitors_05_04_2024_00_06_17_5.0_300_5.0.csv'
    data = pd.read_csv(visitors_file)

    visitors_stats_file = '../../output/visitors/visitors_05_04_2024_00_06_17_5.0_300_5.0_rates.csv'
    stats = pd.read_csv(visitors_stats_file)

    video_builder = VideoBuilder("", f"{data['n'].iloc[0]}_{data['l'].iloc[0]}_5.0_pbc_viz.mp4").set_width(
        video_width).set_height(video_height)

    timesteps = data['time'].unique()
    for timestep in timesteps:
        timestep_data = data[data['time'] == timestep]

        video_builder.create_frame()

        draw_visitors(video_builder, timestep_data)

        if show_stats:
            stats_data = stats[stats['time'] == timestep]
            draw_stats(video_builder, stats_data)

        video_builder.push_frame()

    video_builder.render()


def main():
    parser = argparse.ArgumentParser(description='Process boolean argument')
    parser.add_argument('--show-stats', action='store_true', help='Show stats')
    args = parser.parse_args()
    render_visitors(args.show_stats)


if __name__ == '__main__':
    main()
