/*
 * The MIT License
 *
 * Copyright (c) <2012> <Richard Lavoie>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.imagegallery.comparative;


/**
 * File association group for a path and a set of files
 * 
 * @author Richard Lavoie
 * @since 1.0
 */
public class FilePair implements Comparable<FilePair> {

	private String baseRoot;
	private String name;
	private String parentPath;

	public FilePair(String folder, String name) {
		baseRoot = folder;
		this.name = name;
		this.parentPath = name.substring(0, name.lastIndexOf("/"));
		this.parentPath = parentPath.substring(0, parentPath.lastIndexOf("/"));
	}
	
	public String getBaseRoot() {
		return baseRoot;
	}
	
	public String getName() {
		return name;
	}
	
	public String getParentPath() {
		return parentPath;
	}

	public int compareTo(FilePair o) {
		return baseRoot.compareTo(o.baseRoot);
	}
	
	public String toString() {
		return baseRoot + ":" + name;
	}
	
}