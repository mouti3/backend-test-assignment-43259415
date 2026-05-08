package com.telepaxx.assignment.dto;

import java.util.List;

public record PageResponse<T>(List<T> data, long total) {
}
