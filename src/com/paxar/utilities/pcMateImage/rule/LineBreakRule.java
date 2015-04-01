package com.paxar.utilities.pcMateImage.rule;

import java.util.ArrayList;
import java.util.List;
import com.paxar.utilities.pcMateImage.component.TextSize;

public interface LineBreakRule
{
	public List<String> split(TextSize text, int dpi);
}