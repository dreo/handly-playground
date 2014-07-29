/*******************************************************************************
 * Copyright (c) 2014 1C LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Vladimir Piskarev (1C) - initial API and implementation
 *******************************************************************************/
package org.eclipse.handly.examples.basic.ui;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.handly.examples.basic.ui.internal.FooActivator;
import org.eclipse.handly.internal.examples.basic.ui.model.FooFileFactory;
import org.eclipse.handly.internal.examples.basic.ui.outline.FooOutlinePage;
import org.eclipse.handly.model.ISourceFileFactory;
import org.eclipse.handly.xtext.ui.editor.HandlyDirtyStateEditorSupport;
import org.eclipse.handly.xtext.ui.editor.HandlyXtextDocument;
import org.eclipse.handly.xtext.ui.editor.HandlyXtextEditorCallback;
import org.eclipse.handly.xtext.ui.editor.HandlyXtextReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.xtext.ui.editor.DirtyStateEditorSupport;
import org.eclipse.xtext.ui.editor.IXtextEditorCallback;
import org.eclipse.xtext.ui.editor.folding.FoldedPosition;
import org.eclipse.xtext.ui.editor.folding.IFoldingRegionProvider;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.XtextDocument;
import org.eclipse.xtext.ui.editor.outline.IOutlineNode;
import org.eclipse.xtext.ui.editor.outline.impl.OutlinePage;
import org.eclipse.xtext.ui.util.DisplayRunHelper;

import com.google.common.collect.Iterables;
import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Use this class to register components to be used within the IDE.
 * <p>
 * Note: Xtext-generated {@link FooActivator} implementation assumes 
 * that this class lives in this package. Don't rename/move this class.
 */
public class FooUiModule
    extends AbstractFooUiModule
{
    public FooUiModule(AbstractUIPlugin plugin)
    {
        super(plugin);
    }

    public Class<? extends IFoldingRegionProvider> bindIFoldingRegionProvider()
    {
        return NullFoldingRegionProvider.class;
    }

//    @Override
//    public Class<? extends IContentOutlinePage> bindIContentOutlinePage()
//    {
//        return XtextOutlinePage.class;
//    }
//
//    @Override
//    public Class<? extends ILabelProvider> bindILabelProvider()
//    {
//        return FooLabelProvider.class;
//    }
//
//    public Class<? extends IOutlineTreeProvider> bindIOutlineTreeProvider()
//    {
//        return FooOutlineTreeProvider.class;
//    }
//
//    public Class<? extends IOutlineTreeStructureProvider> bindIOutlineTreeStructureProvider()
//    {
//        return FooOutlineTreeProvider.class;
//    }

    @Override
    public Class<? extends IContentOutlinePage> bindIContentOutlinePage()
    {
        return FooOutlinePage.class;
    }

// the following bindings are required for Handly/Xtext integration:

    @Override
    public Class<? extends IReconciler> bindIReconciler()
    {
        return HandlyXtextReconciler.class;
    }

    public Class<? extends XtextDocument> bindXtextDocument()
    {
        return HandlyXtextDocument.class;
    }

    public Class<? extends DirtyStateEditorSupport> bindDirtyStateEditorSupport()
    {
        return HandlyDirtyStateEditorSupport.class;
    }

    public void configureXtextEditorCallback(Binder binder)
    {
        binder.bind(IXtextEditorCallback.class).annotatedWith(
            Names.named(HandlyXtextEditorCallback.class.getName())).to(
            HandlyXtextEditorCallback.class);
    }

    public Class<? extends ISourceFileFactory> bindISourceFileFactory()
    {
        return FooFileFactory.class;
    }

    private static class NullFoldingRegionProvider
        implements IFoldingRegionProvider
    {
        @Override
        public Collection<FoldedPosition> getFoldingRegions(
            IXtextDocument xtextDocument)
        {
            return Collections.emptySet();
        }
    }

    private static class XtextOutlinePage
        extends OutlinePage
    {
        @Override
        protected void refreshViewer(final IOutlineNode rootNode,
            final Collection<IOutlineNode> nodesToBeExpanded,
            final Collection<IOutlineNode> selectedNodes)
        {
            DisplayRunHelper.runAsyncInDisplayThread(new Runnable()
            {
                public void run()
                {
                    long start = System.currentTimeMillis();
                    try
                    {
                        TreeViewer treeViewer = getTreeViewer();
                        if (!treeViewer.getTree().isDisposed())
                        {
                            treeViewer.setInput(rootNode);
                            treeViewer.expandToLevel(1);
                            treeViewer.setExpandedElements(Iterables.toArray(
                                nodesToBeExpanded, IOutlineNode.class));
                            treeViewer.setSelection(new StructuredSelection(
                                Iterables.toArray(selectedNodes,
                                    IOutlineNode.class)));
                            treeUpdated();
                        }
                    }
                    catch (Throwable t)
                    {
                    }
                    long duration = System.currentTimeMillis() - start;
                    System.out.println("Outline refresh: " + duration + " ms");
                }
            });
        }
    }
}
