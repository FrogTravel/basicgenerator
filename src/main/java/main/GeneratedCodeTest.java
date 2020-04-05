package main;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;
import jdk.nashorn.internal.ir.LabelNode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class GeneratedCodeTest {
    public static void main(String[] args) throws IOException {
        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv = null;

        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "gen/HelloGen", null, "java/lang/Object", null);
        mv = cw.visitMethod(0, "a", "()Ljava/lang/Integer;", null, null);
        mv.visitInsn(ICONST_1);

        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
        mv.visitInsn(ARETURN);
        mv.visitCode();
        cw.visitEnd();

        //save bytecode into disk
        FileOutputStream out = new FileOutputStream("/Users/ekaterina/IdeaProjects/jsonparsertest/src/main/java/main/gen/HelloGen.class");
        out.write(cw.toByteArray());
        out.close();
    }
}
