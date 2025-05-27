package com.corp.pixelro.global.util;

import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;

public class NoHtmlCharacterEscapes extends CharacterEscapes {

	private final int[] asciiEscapes;

	public NoHtmlCharacterEscapes() {
		asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
		// 기본 설정을 그대로 두고, HTML 관련 문자만 비활성화
		asciiEscapes['<'] = CharacterEscapes.ESCAPE_NONE;
		asciiEscapes['>'] = CharacterEscapes.ESCAPE_NONE;
		asciiEscapes['&'] = CharacterEscapes.ESCAPE_NONE;
		asciiEscapes['('] = CharacterEscapes.ESCAPE_NONE;
		asciiEscapes[')'] = CharacterEscapes.ESCAPE_NONE;
		asciiEscapes['"'] = CharacterEscapes.ESCAPE_STANDARD;
		asciiEscapes['\''] = CharacterEscapes.ESCAPE_STANDARD;
	}

	@Override
	public int[] getEscapeCodesForAscii() {
		return asciiEscapes;
	}

	@Override
	public SerializedString getEscapeSequence(int ch) {
		return null;
	}
}
