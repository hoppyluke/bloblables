package com.supergreenowl.blobables.game.presentation;

import android.graphics.Color;

public class ColourFade {

	public static final byte A = 0;
	public static final byte R = 1;
	public static final byte G = 2;
	public static final byte B = 3;
	
	/**
	 * Splits a dense colour int into ARGB components.
	 * @param colour Colour int.
	 * @return New array of colour components.
	 */
	public static int[] split(int colour) {
		return new int[] { Color.alpha(colour), Color.red(colour), Color.green(colour), Color.blue(colour) };
	}
	
	/**
	 * Splits a dense colour int into ARGB components. 
	 * @param colour Colour int.
	 * @param argb ARGB components array to put result into.
	 */
	public static void split(int colour, int[] argb) {
		if(argb.length != 4) throw new IllegalArgumentException();
		
		argb[A] = Color.alpha(colour);
		argb[R] = Color.red(colour);
		argb[G] = Color.green(colour);
		argb[B] = Color.blue(colour);		
	}
	
	/**
	 * Merges an ARGB array into a dense colour int.
	 * @param argb Array to merge.
	 * @return Dense colour int representing same colour value.
	 */
	public static int merge(int[] argb) {
		if(argb.length != 4) throw new IllegalArgumentException();
		
		return Color.argb(argb[A], argb[R], argb[G], argb[B]);
	}
	
	/**
	 * Calculates the amounts to increment a colour by to reach the destination colour
	 * from the source colour in the specified number of steps.
	 * Source and destination are specified as colour component arrays.
	 * @param from Source colour.
	 * @param to Destination colour.
	 * @param steps Number of steps to reach destination from source.
	 * @return Increment components to fade from source to destination colour.
	 */
	public static int[] increment(int[] from, int[] to, int steps) {
		int[] increment = new int[4];
		
		for(int i = A; i <= B; i++) {
			int inc = (to[i] - from[i]) / steps;
			increment[i] = inc;
		}
		
		return increment;
	}
	
	/**
	 * Calculates the amounts to increment a colour by to reach the destination colour
	 * from the source colour in the specified number of steps. Source and destination
	 * are specified as dense colour ints.
	 * @param from Source colour.
	 * @param to Destination colour.
	 * @param steps Number of steps to reach destination from source.
	 * @return Increment components to fade from source to destination colour.
	 */
	public static int[] increment(int from, int to, int steps) {
		return increment(split(from), split(to), steps);
	}
	
	/**
	 * Calculates the colour increment to fade in or out to/from the specified colour from/to transparent.
	 * @param argb Colour to fade to or frame.
	 * @param steps Number of steps to complete fade.
	 * @param fadeIn If true fades in to the colour from transparent; otherwise fades out to transparent.
	 * @return ARGB components to increment by each step.
	 */
	public static int[] increment(int[] argb, int steps, boolean fadeIn) {
		int[] transparent = transparent(argb);
		int[] start = fadeIn ? transparent : argb;
		int[] end = fadeIn ? argb : transparent;
		return increment(start, end, steps);
	}
	
	/**
	 * Calculates the colour increment to fade in or out to/from the specified colour from/to transparent.
	 * @param argb Colour to fade to or frame.
	 * @param steps Number of steps to complete fade.
	 * @param fadeIn If true fades in to the colour from transparent; otherwise fades out to transparent.
	 * @return ARGB components to increment by each step.
	 */
	public static int[] increment(int colour, int steps, boolean fadeIn) {
		return increment(split(colour), steps, fadeIn);
	}
	
	/**
	 * Returns a fully opaque version of the specified colour.
	 * @param argb
	 * @return
	 */
	public static int[] opaque(int[] argb) {
		int[] result = argb.clone();
		result[A] = 255;
		return result;
	}
	
	/**
	 * Returns a fully transparent version of the specified colour.
	 * @param argb
	 * @return
	 */
	public static int[] transparent(int[] argb) {
		int[] result = argb.clone();
		result[A] = 0;
		return result;
	}
}