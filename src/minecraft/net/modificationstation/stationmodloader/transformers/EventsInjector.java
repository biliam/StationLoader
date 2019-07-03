package net.modificationstation.stationmodloader.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

/**
 * The class that patches StationModLoader's events into Minecraft's bytecode, so we don't need to edit anything
 * 
 * @author mine_diver
 *
 */

@SuppressWarnings("unchecked")
public class EventsInjector {    
    
    /**
     * List of patch functions, so we can access them by numbers
     */
    final Consumer<ClassNode> patch[];
    
    /**
     * Closed constructor that sets patch[]
     */
    EventsInjector() {
        final List<Consumer<ClassNode>> list = new ArrayList<Consumer<ClassNode>>();
        list.add(this::patchMinecraft);
        patch = list.toArray(new Consumer[]{});
    }
    
    public void patchMinecraft(ClassNode classNode) {
        Iterator<MethodNode> methodIt = classNode.methods.iterator();
        for (MethodNode method = methodIt.next();methodIt.hasNext();method = methodIt.next()) {
        	if (method.name.equals("<init>") && method.desc.equals("(Ljava/awt/Component;Ljava/awt/Canvas;Lnet/minecraft/client/MinecraftApplet;IIZ)V"))
        		method.instructions.insertBefore(method.instructions.get(method.instructions.size() - 2), ClassesInfo.Minecraft.MCInitializationEvent);
        }
    }
    
    /**
     * Function that adds event by event path into InsnList
     * 
     * @param toAdd
     * @param eventPath
     */
    private static void addEvent(InsnList toAdd, String eventPath) {
        toAdd.add(new TypeInsnNode(NEW, eventPath));
		toAdd.add(new InsnNode(DUP));
		toAdd.add(new MethodInsnNode(INVOKESPECIAL, eventPath, "<init>", "()V", false));
		toAdd.add(new MethodInsnNode(INVOKEVIRTUAL, eventPath, "process", "()Lnet/modificationstation/stationmodloader/events/ModEvent;", false));
		toAdd.add(new InsnNode(POP));
    }
    
    /**
     * The class that provides patch info for Minecraft classes.
     * 
     * @author mine_diver
     *
     */
    private static class ClassesInfo {
    	static class Minecraft {
    		static final InsnList StationModLoaderInit = new InsnList();
    		static final InsnList MCInitializationEvent = new InsnList();
    		
    		static {
    			StationModLoaderInit.add(new MethodInsnNode(INVOKESTATIC, "net/modificationstation/stationmodloader/StationModLoader", "init", "()V", false));
    			
    			addEvent(MCInitializationEvent, "net/modificationstation/stationmodloader/events/MCInitializationEvent");
    		}
    	}
    }
}