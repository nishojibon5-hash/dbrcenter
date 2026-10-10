#!/bin/bash
while true; do
  python3 -m http.server 3000 --bind 0.0.0.0
  sleep 1
done
