package mmo.util;

public class SafeTimer {
	private long startTime;
	private long interval;// 间隔毫秒
	boolean isfirst = true;

	public SafeTimer() {
	}
	public void changeInterval( long dur){
		this.interval=dur;
	}
	public SafeTimer(long dur) {
		start(dur);
	}

	public void start(long dur) {
		this.interval = dur;
		startTime = System.currentTimeMillis();
		isfirst = true;
	}

	/**
	 * 
	 * @param dur 间隔时间
	 * @param elapse 时间偏差
	 * @param now 当前时间
	 */
	private void restart(long dur, long elapse,long now) {
		this.interval = dur;
		if(elapse<0){
			System.err.println("出现异常elapse>interval"+elapse+Thread.currentThread());
			startTime=now;
		}else{
			startTime = now - elapse;
		}
	}

	public boolean isOK() {
		long curTime = System.currentTimeMillis();
		if (curTime - startTime >= interval) 
			return true;
		return false;
	}
	public boolean isOK(long now) {
		if (now - startTime >= interval) 
			return true;
		return false;
	}
	/**
	 * 距离间隔还差多少
	 * @return
	 */
	public long remainTimeToIntvl() {
		long curTime = System.currentTimeMillis();
		if (!isOK()) 
			return interval - (curTime - startTime);
		return 0;
	}
	/**
	 * 距离间隔还差多少秒
	 * @return
	 */
	public int remainSecondsToIntvl() {
		return remainTimeToIntvl() / 1000 <= 0 ? 0 : (int)(remainTimeToIntvl() / 1000);
	}
	/**
	 * 恢复初始化
	 * @return
	 */
	public void reset() {
		startTime = 0;
		interval = 0;
		isfirst = true;
	}
	/**
	 * 第一次到时
	 * @param now
	 * @return
	 */
	public boolean isFirstOK(long now)
	{
		if (isfirst && now - startTime >= interval) 
		{
			isfirst = false;
			return true;
		}
		return false;
	}

	/**
	 *  是否到了固定的间隔 补帧
	 * @param now
	 * @return
	 */
	public boolean isIntervalOK(long now) {
		if (now - startTime >= interval) {
			restart(interval, now - startTime - interval,now);
			return true;
		}
		return false;
	}
	

	/**
	 * 是否到了固定的间隔 不补帧
	 * @param now
	 * @return
	 */
	public boolean isInterval(long now) {
		if (now - startTime >= interval) {
			restart(interval, 0, now);
			return true;
		}
		return false;
	}
	
	public void setInterval(long interval) {
		this.interval = interval;
	}
	
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	public long getStartTime() {
		return startTime;
	}
	
	
}
