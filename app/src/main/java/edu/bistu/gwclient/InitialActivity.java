package edu.bistu.gwclient;

import edu.bistu.gwclient.automata.Automata;

public class InitialActivity extends CustomActivity
{

    @Override
    protected void setUI()
    {
        setContentView(R.layout.activity_initial);
    }

    @Override
    protected void setHandler()
    {
        handler = new Handler();
    }

    @Override
    protected void lockUI()
    {

    }

    @Override
    protected void unlockUI()
    {

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                /* 启动自动机线程 */
                Memory.automata = new Automata();
                Memory.automataThread = new Thread(Memory.automata);
                Memory.automataThread.start();
            }
        }).start();
    }

    @Override
    public void onBackPressed()
    {
        //do nothing
    }
}
