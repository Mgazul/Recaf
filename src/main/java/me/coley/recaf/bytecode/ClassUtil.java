package me.coley.recaf.bytecode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.coley.recaf.util.Classpath;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import me.coley.recaf.config.impl.ConfASM;

/**
 * Objectweb ASM ClassNode utilities.
 * 
 * @author Matt
 */
public class ClassUtil {

	/**
	 * Creates a ClassNode from the given bytecode array.
	 * 
	 * @param bs
	 *            Array of class bytecode.
	 * @return The node representation.
	 * @throws IOException
	 *             Thrown if the array could not be streamed.
	 */
	public static ClassNode getNode(byte[] bs) throws IOException {
		return getNode(new ClassReader(new ByteArrayInputStream(bs)));
	}

	/**
	 * Creates a ClassNode from the given class.
	 *
	 * @param c
	 *            The target class.
	 * @return Node generated from the given class.
	 * @throws IOException
	 *             If an exception occurs while loading the class.
	 */
	public static ClassNode getNode(Class<?> c) throws IOException {
		ClassReader cr = new ClassReader(Classpath.getClass(c));
		return getNode(cr);
	}

	/**
	 * Common-base of public-facing <i>'getNode'</i> calls.
	 * 
	 * @param cr
	 *            Reader to parse class with.
	 * @return ClassNode from reader.
	 */
	private static ClassNode getNode(ClassReader cr) {
		ClassNode cn = new ClassNode();
		cr.accept(cn, ConfASM.instance().getInputFlags());
		return cn;
	}

	/**
	 * Writes a ClassNode to a byte array.
	 *
	 * @param cn
	 *            The target ClassNode.
	 * @return ByteArray representation of cn.
	 * @throws Exception
	 *             Thrown when the node could not be regenerated. There are a
	 *             few potential causes for this:
	 *             <ul>
	 *             <li>Frames could not be generated due to invalid
	 *             bytecode.</li>
	 *             <li>Frames could not be generated due to inclusion of
	 *             outdated opcodes like <i>JSR/RET</i></li>
	 *             </ul>
	 */
	public static byte[] getBytes(ClassNode cn) throws Exception {
		ClassWriter cw = new NodeParentWriter(ConfASM.instance().getOutputFlags());
		cn.accept(cw);
		return cw.toByteArray();
	}

}
