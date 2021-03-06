/*
 * 
 * File: Configurations.java
 * 
 * Copyright (C) 2015, Nuance Communications Inc. All Rights Reserved.
 * 
 */

package com.nuance.nina.configurations;
/**
 * Class that defines the application variables
 * This class is generated by a script before donwloading and the variables are populated from the user account provided by Nuance.
 *
 */
public class Configurations
{
	/**
	 * NMAID ID
	 */
	public static String NMAID_ID = "ConUHackCo017_ConUHackApp017_20160112_213107";
	/**
	 * NMAID Key as string
	 */
	public static final String NMAID_KEY_STRING = "0xcb 0x25 0xfa 0x52 0x0a 0xfa 0xe2 0xec 0x20 0xa3 0xc0 0x3c 0x85 0x69 0xdb 0x31 0x32 0x0f 0x19 0xe9 0x8c 0x6d 0x60 0x12 0xb4 0x87 0xd6 0x8e 0x87 0xdd 0xe1 0x95 0x36 0x73 0x1b 0x24 0xcf 0x1e 0xa5 0x2f 0xda 0x1d 0x5b 0x57 0x60 0x37 0xba 0x52 0x52 0x1d 0x31 0x0b 0xa9 0x98 0x7e 0x5f 0x6d 0xb3 0x92 0x57 0xd1 0x91 0x73 0x81";
	/**
	 * Application Name
	 */
	public static String APPLICATION_NAME = "ConUHackApp017";
	/**
	 * Application version
	 */
	public static String APPLICATION_VERSION = "0.0";
	/**
	 * Company Name
	 */
	public static String COMPANY_NAME = "ConUHackCo017";

	/**
	 * Server IP address or host name to connect to
	 */
	public static String GATEWAY_HOST = "nim-rd.nuance.mobi";

	/**
	 * Port number
	 */
	public static int GATEWAY_PORT = 443;

	/**
	 * Gateway SSL 
	 */
	public static boolean GATEWAY_USE_SSL = true;

	/**
	 * Gateway SSL certificate
	 */
	public static boolean GATEWAY_VERIFY_SSL_CERTIFICATE = false;

	/**
	 * Server Application name
	 */
	public static String SERVER_APPLICATION_NAME = "NinaCloud";

	/**
	 * Default Nina voice for TTS prompts. The spelling should be exactly the same of a voice enabled for your application by Nuance
	 */
	public static String TTS_VOICE_NAME = "Carol";
	/**
	 * Default Nina locale for TTS prompts. The spelling should be exactly the same of a locale enabled for your application by Nuance
	 */
	public static String TTS_VOICE_LOCALE = "en";
}
