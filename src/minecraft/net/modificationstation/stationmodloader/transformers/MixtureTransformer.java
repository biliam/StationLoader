package net.modificationstation.stationmodloader.transformers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import net.modificationstation.classloader.ClassLoaderReplacer;
import net.modificationstation.classloader.IClassTransformer;
import net.modificationstation.stationmodloader.mixture.Mixture;
import net.modificationstation.stationmodloader.mixture.Mixture.Intervene;
import net.modificationstation.stationmodloader.mixture.Mixture.Intervene.ShiftType;

import static org.objectweb.asm.Opcodes.*;

public class MixtureTransformer implements IClassTransformer {
	
	private static final Map<String, Set<ClassNode>> mixtures = new HashMap<String, Set<ClassNode>>();
	
	public static void registerMixture(String className) {
		try {
			byte[] mixtureBytes = ClassLoaderReplacer.INSTANCE.classLoader.getClassBytes(className);
			ClassNode mixtureNode = new ClassNode();
			ClassReader classReader = new ClassReader(mixtureBytes);
			classReader.accept(mixtureNode, 0);
			for (AnnotationNode ann : mixtureNode.invisibleAnnotations)
				if (ann.desc.equals(Type.getDescriptor(Mixture.class))) {
					String name = ((Type)ann.values.get(1)).getClassName();
					if (!mixtures.containsKey(name))
						mixtures.put(name, new HashSet<ClassNode>());
					mixtures.get(name).add(mixtureNode);
					break;
				}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public MixtureTransformer() {
		registerMixture("net.modificationstation.stationmodloader.mixture.MixtureTest");
	}
	
	@Override
	public byte[] transform(String name, byte[] bytes) {
		if (mixtures.get(name) != null) {
			for (ClassNode cn : mixtures.get(name))
				System.out.println(cn.name);
			ClassNode targetNode = new ClassNode();
			ClassReader targetClassReader = new ClassReader(bytes);
			targetClassReader.accept(targetNode, 0);
			for (ClassNode mixtureNode : mixtures.get(name))
				applyMixture(targetNode, mixtureNode);
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			targetNode.accept(classWriter);
			bytes = classWriter.toByteArray();
		}
		return bytes;
	}
	
	private void applyMixture(ClassNode targetNode, ClassNode mixtureNode) {
		for (MethodNode method : mixtureNode.methods)
			if (method.visibleAnnotations != null)
				for (AnnotationNode mAnn : method.visibleAnnotations)
					if (mAnn.desc.equals(Type.getDescriptor(Intervene.class))) {
						for (MethodNode targetMethod : targetNode.methods) {
							if (targetMethod.desc.equals(method.desc) && targetMethod.name.equals(method.name)) {
								InsnList instructions = new InsnList();
								instructions.add(method.instructions);
								applyRedirect(instructions, mixtureNode.name, targetNode.name);
								if (mAnn.values != null)
									for (int i = 0; i < mAnn.values.size(); i+=2) {
										if (((String)mAnn.values.get(i)).equals("shift")) {
											String[] params = ((String[])mAnn.values.get(i+1));
											for (int j = 0; j < params.length; j+=2)
												if (params[j].equals(Type.getDescriptor(ShiftType.class))) {
													ShiftType type = ShiftType.valueOf(params[j+1]);
													if (type == ShiftType.BEFORE) {
														InsnList insns = new InsnList();
														for (int k = 0; k < instructions.size() - 2; k++)
															insns.add(instructions.get(k));
														targetMethod.instructions.insertBefore(targetMethod.instructions.get(0), insns);
													}
													else if (type == ShiftType.OVERWRITE)
														targetMethod.instructions = instructions;
													else if (type == ShiftType.AFTER)
														targetMethod.instructions.insert(targetMethod.instructions.get(targetMethod.instructions.size() - 3), instructions);
													break;
												}
											break;
										}
									}
								else {
									targetMethod.instructions = instructions;
								}
								break;
							}
						}
						break;
					}
	}
	
	private void applyRedirect(InsnList instructions, String redirectFrom, String redirectTo) {
		Iterator<AbstractInsnNode> instructionIt = instructions.iterator();
		for (AbstractInsnNode insn = instructionIt.next();instructionIt.hasNext();insn = instructionIt.next())
			if (insn.getOpcode() == GETFIELD && ((FieldInsnNode)insn).owner.equals(redirectFrom))
				((FieldInsnNode)insn).owner = redirectTo;
	}
}
