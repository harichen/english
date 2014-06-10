package com.happy.english.service;

import java.io.IOException;
import com.happy.english.constant.Const;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class PlayMusicService extends Service
{
    MediaPlayer mPlayer;
    private myBinder mybinder;
    private boolean isPause;
    private boolean isPrepared = false;

    public PlayMusicService()
    {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mPlayer = new MediaPlayer();
        mybinder = new myBinder();
        mPlayer.setOnCompletionListener(new OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                play(Const.MISSIONINDEX);
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException(
                "Not yet implemented");
    }

    public void play(String string)
    {
        try
        {
            mPlayer.reset();
            Log.e("mPlayer", "reset");
            isPrepared = false;
            mPlayer.setDataSource(string);
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp)
                {
                    mPlayer.start();
                    isPrepared = true;
                    Log.e("onPrepared", "start");
                }
            });
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public class myBinder extends Binder
    {
        public void play(String string)
        {
            isPause = false;
            PlayMusicService.this.play(string);
        }

        public void play()
        {
            isPause = false;
            mPlayer.start();
        }

        public boolean isPause()
        {
            return isPause;
        }

        public boolean isPlaying()
        {
            return mPlayer.isPlaying();
        }

        public void pause()
        {
            isPause = true;
            mPlayer.pause();
        }

        public String getCurrentTime()
        {
            return convertTimeFormat(getCurrentPosition());
        }

        public CharSequence getTotalTime()
        {
            return convertTimeFormat(getDuration());
        }

        public int getDuration()
        {
            if (isPrepared)
            {
                return mPlayer.getDuration() / 1000;
            }
            return 0;
        }

        public int getCurrentPosition()
        {
            if (isPrepared)
            {
                return mPlayer.getCurrentPosition() / 1000;
            }
            return 0;
        }

        public void seekTo(int progress)
        {
            mPlayer.seekTo(progress);
        }

        private String convertTimeFormat(int times)
        {
            StringBuilder buf = new StringBuilder();
            int minute = times / 60;
            add0(buf, minute);
            buf.append(":");
            int second = times % 60;
            add0(buf, second);
            return buf.toString();
        }

        private void add0(StringBuilder buf, int times)
        {
            if (times < 10)
            {
                buf.append("0" + times);
            }
            else
            {
                buf.append(times);
            }
        }
    }
}
