cmake_minimum_required(VERSION 3.16)
project(AfterTheFallFactory LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

find_package(raylib REQUIRED)

add_executable(after_the_fall_factory src/main.cpp)
target_link_libraries(after_the_fall_factory PRIVATE raylib)

if (UNIX AND NOT APPLE)
  target_link_libraries(after_the_fall_factory PRIVATE m pthread dl rt X11)
endif()
