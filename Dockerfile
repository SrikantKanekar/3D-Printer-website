FROM openjdk:8-jdk
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/install/example/ /app/
WORKDIR /app/bin
ENV JWT_SECRET=123456
ENV NOTIFICATION_EMAIL=3dprinterwebsite1@gmail.com
ENV NOTIFICATION_PASSWORD=3d-printer-website$
ENV RAZORPAY_KEY=rzp_test_oude3vJ48mVrXI
ENV RAZORPAY_SECRET=gTDCzfgCqehCDRaJC10PYn7M
CMD ["./example"]
