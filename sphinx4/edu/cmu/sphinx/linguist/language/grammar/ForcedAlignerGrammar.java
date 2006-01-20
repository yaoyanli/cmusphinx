/*
 * Copyright 1999-2002 Carnegie Mellon University.  
 * Portions Copyright 2002 Sun Microsystems, Inc.  
 * Portions Copyright 2002 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 * 
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL 
 * WARRANTIES.
 *
 */

package edu.cmu.sphinx.linguist.language.grammar;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import edu.cmu.sphinx.util.LogMath;

/**
 * Creates a grammar from a reference sentence. It is a constrained
 * grammar that represents the sentence only.
 * <p/>
 * Note that all grammar probabilities are maintained in the LogMath
 * log base
 */
public class ForcedAlignerGrammar extends Grammar {

    protected GrammarNode finalNode;

    /**
     * Create class from reference text (not implemented).
     */
    protected GrammarNode createGrammar() {
        throw new Error( "Not implemented" );
    }

    /**
     * Creates the grammar
     */
    protected GrammarNode createGrammar(String referenceText)
	throws NoSuchMethodException {

        initialNode = createGrammarNode(false);
        finalNode = createGrammarNode(true);
        createForcedAlignerGrammar( initialNode, finalNode, referenceText );

        return initialNode;
    }

    /**
     * Create a branch of the grammar that corresponds to a transcript.  For each
     * word create a node, and link the nodes with arcs.  The branch is connected to
     * the initial node iNode, and the final node fNode.
     *
     * @param iNode
     * @param fNode
     * @param transcript
     * @return the first node of this branch
     * @throws NoSuchMethodException
     */
    protected GrammarNode createForcedAlignerGrammar(GrammarNode iNode, GrammarNode fNode, String transcript) throws NoSuchMethodException {
        final float logArcProbability = LogMath.getLogOne();

        StringTokenizer tok = new StringTokenizer(transcript);

        GrammarNode firstNode = null;
        GrammarNode lastNode = null;

	while (tok.hasMoreTokens()) {

	    String token;
	    token = tok.nextToken();

            GrammarNode prevNode = lastNode;
            lastNode = createGrammarNode(token);
            if( firstNode==null ) firstNode = lastNode;

            if( prevNode != null ) {
                prevNode.add(lastNode, logArcProbability);
	}
    }

        iNode.add(firstNode,logArcProbability);
        lastNode.add(fNode,logArcProbability);

        return firstNode;
    }
}
