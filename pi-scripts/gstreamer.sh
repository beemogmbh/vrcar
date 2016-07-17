#script to start stream on pi
raspivid -t 0 -awb fluorescent -ex off -h 480 -w 640 -fps 30 -b 500000 -o - | gst-launch-1.0 -v fdsrc ! h264parse !  rtph264pay config-interval=1 pt=96 ! gdppay ! tcpserversink host=0.0.0.0 port=5000
