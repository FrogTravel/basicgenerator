package main;

import com.google.gson.Gson;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.*;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class Main {
    ClassWriter cw = new ClassWriter(0);
    MethodVisitor mv = null;

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.generate();
    }

    private void generate() throws IOException {
        Gson gson = new Gson();

        File file = new File("/Users/ekaterina/IdeaProjects/jsonparsertest/src/main/java/main/denis_example.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        StringBuilder lines = new StringBuilder();
        String st;
        while ((st = bufferedReader.readLine()) != null) {
            lines.append(st);
            System.out.println(st);
        }

        Entity entity = gson.fromJson(lines.toString(), Entity.class);

        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "gen/HelloGen", null, "java/lang/Object", null);

        System.out.println("cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, \"gen/HelloGen\", null, \"java/lang/Object\", null);");

        parseTree(entity);


        cw.visitEnd();
        System.out.println("cw.visitEnd();\n");

        //save bytecode into disk
        FileOutputStream out = new FileOutputStream("/Users/ekaterina/IdeaProjects/jsonparsertest/src/main/java/main/gen/HelloGen.class");
        out.write(cw.toByteArray());
        out.close();
    }

    private void parseTree(Entity tree) {
        switch (tree.type) {
            case "ROUTINE":
                Entity methodNameEntity = findByType(tree.children, "IDENTIFIER");
                Entity methodTypeEntity = findByType(tree.children, "UNIT_REF");
                Entity methodEntityList = findByType(tree.children, "ENTITY_LIST");

                if (methodNameEntity == null || methodTypeEntity == null || methodEntityList == null)
                    break;

                mv = cw.visitMethod(0, methodNameEntity.value, "()Ljava/lang/" + methodTypeEntity.value+ ";", null, null);
                System.out.println("mv = cw.visitMethod(0, \"" +  methodNameEntity.value + "\", \"()Ljava/lang/" + methodTypeEntity.value + ";\", null, null);");

                for (Entity children : methodEntityList.children) {
                    parseTree(children);
                }

                mv.visitCode();
                System.out.println("mv.visitCode();");

                break;
            case "LITERAL":
                Entity literalTypeEntity = findByType(tree.children, "UNIT_REF");
                if (literalTypeEntity == null)
                    break;

                switch (literalTypeEntity.value){
                    case "Integer":
                        generateIntegerConstant();
                }
                break;
            case "RETURN":
                for (Entity children : tree.children) {
                    parseTree(children);
                }
                mv.visitInsn(ARETURN);
                System.out.println("mv.visitInsn(ARETURN);");
                break;
        }


    }

    private void generateIntegerConstant(){
        mv.visitInsn(ICONST_1);
        System.out.println("mv.visitInsn(ICONST_1);\n");

        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
        System.out.println("mv.visitMethodInsn(INVOKESTATIC, \"java/lang/Integer\", \"valueOf\", \"(I)Ljava/lang/Integer;\", false);");
    }

    private Entity findByType(List<Entity> entities, String type) {
        for (Entity entity : entities) {
            if (entity.type.equals(type)) {
                return entity;
            }
        }

        return null;
    }
}
