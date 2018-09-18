package cgtools;

import java.time.Duration;
import java.time.Instant;

/**
 * A simple stopwatch
 */
public class StopWatch {

   private Instant startTime;
   private Instant endTime;
   private Duration duration;
   private boolean isRunning = false;

   public void start() {
      if (isRunning) {
         throw new RuntimeException("Stopwatch is already running.");
      }
      this.isRunning = true;
      startTime = Instant.now();
   }

   public void stop() {
      endTime = Instant.now();
      if (!isRunning) {
         throw new RuntimeException("Stopwatch has not been started yet");
      }
      isRunning = false;
      Duration result = Duration.between(startTime, endTime);
      if (this.duration == null) {
         this.duration = result;
      } else {
         this.duration = duration.plus(result);
      }
   }

   public Duration getElapsedTime() {
      return this.duration;
   }

   public void reset() {
      if (this.isRunning) {
         this.stop();
      }
      this.duration = null;
   }

   public Instant getStartTime() {
      return startTime;
   }

   public Instant getEndTime() {
      return endTime;
   }
}