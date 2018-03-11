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

import hudson.model.Action;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Actionable;

import java.io.Serializable;

/**
 * A project action with a group of image files associated by a path and a set of files
 *
 * @author Richard Lavoie
 * @since 0.2
 */
public class ComparativeImagesGalleryProjectAction implements Action {
   
	private final String title;
	/**
	 * The array of images.
	 */
	private final FilePairTree tree;
	/**
	 * The image width.
	 */
	private final Integer imageWidth;

    /**
     * The inner image width
     */
    private Integer imageInnerWidth;
    
    private final AbstractProject project;
    
	public AbstractProject getProject() {
		return project;
	}

	public ComparativeImagesGalleryProjectAction(String title, FilePairTree tree, Integer imageWidth, Integer imageInnerWidth, AbstractProject project) {
		this.title = title;
		this.tree = tree;
        //this.imageInnerWidth = imageInnerWidth;
		if(imageInnerWidth != null) {
			this.imageInnerWidth = imageInnerWidth;
		} else {
			this.imageInnerWidth = new Integer(500);
		}
		if(imageWidth != null) {
			this.imageWidth = imageWidth;
		} else {
			this.imageWidth = new Integer(0);
		}
		this.project = project;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the images
	 */
	public FilePairTree getImages() {
		return tree;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getIconFileName() {
		return "/plugin/image-gallery/js/themes/default/galleryIcon30x30.png";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDisplayName() {
		return "Image Gallery";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUrlName() {
		return project.getAbsoluteUrl()+"lastSuccessfulBuild";
	}

	/**
	 * @return the imageWidth
	 */
	public Integer getImageWidth() {
		return imageWidth;
	}

    /**
     * @return the imageInnerWidth
     */
    public Integer getImageInnerWidth() {
        return imageInnerWidth;
    }
}
