# Audio File Setup Instructions

## Current Status

The app is now configured to work in **simulation mode** when no audio file is present. This means:
- Word highlighting will work automatically
- Counter will increment every 30 seconds (simulated audio duration)
- No actual audio will play, but the visual experience will be complete

## Required Files

### 1. Audio File: `mantra_audio.mp3` (Optional for testing)
- Place your MP3 audio file in this directory
- The file should contain the complete Hare Krishna Maha Mantra
- Recommended duration: 30-60 seconds for one complete recitation
- Format: MP3
- Sample rate: 44.1 kHz or 48 kHz
- Bitrate: 128 kbps or higher for good quality

### 2. Timing File: `mantra_timing.ttf`
- Already created in this directory
- Contains timestamps and word indices for synchronization
- Format: `timestamp_in_ms,word_index`
- Each line represents when a specific word should be highlighted

## Audio File Requirements

The MP3 file should contain the mantra recited in this order:
1. हरे कृष्ण, हरे कृष्ण, कृष्ण कृष्ण, हरे हरे|
2. हरे राम, हरे राम, राम राम, हरे हरे||

## Timing File Format

The TTF file contains entries like:
```
0,0      # हरे at 0ms
2000,1   # कृष्ण, at 2000ms
4000,2   # हरे at 4000ms
...
```

## How to Create Your Own Timing File

1. **Record or obtain** your mantra audio file
2. **Analyze the audio** to determine when each word starts
3. **Create timing entries** in the format: `timestamp,word_index`
4. **Test synchronization** by running the app

## Example Audio Sources

- Record your own voice reciting the mantra
- Use a professional recording
- Download from authorized sources
- Create using text-to-speech and then manually adjust timing

## Testing

1. Add your `mantra_audio.mp3` file to this directory
2. Adjust the timing in `mantra_timing.ttf` if needed
3. Build and run the app
4. Test the auto-chant feature to verify synchronization

## Notes

- The app will fall back to default timing if the TTF file is not found
- Audio quality affects the user experience significantly
- Ensure proper pronunciation and pacing in the audio file
- The timing file should match the actual audio content exactly 