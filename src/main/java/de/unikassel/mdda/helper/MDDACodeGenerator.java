package de.unikassel.mdda.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.PUSH;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import de.unikassel.mdda.MDDACodeGen;

public class MDDACodeGenerator implements Constants {
	
	public static boolean DEBUG = false;

	private static final String DIMENSION_SIZE_ARRAY_NAME = "dimensionSizeArray";
	private static final String DIMENSION_SIZE_NAME = "dimensionSize";
	private static final String MULTI_DIMENSIONAL_ARRAY_NAME = "multiDimArray";


	/**
	 * default: target/classes/
	 */
	public static String TARGET_PATH = "target/classes/";
	
	/**
	 * default: src/main/resources/gen/
	 */
	public static String SOURCE_GENERATED_PATH = "src/main/resources/gen/";
	
	private static boolean PRIMITIVE = false;

	private InstructionFactory _factory;
	private ConstantPoolGen _cp;
	private ClassGen _cg;
	private static int[] dimensions;
	private static String fullClassName;
	private static String simpleNameClass;
	private static String arrayClass;
  
	public MDDACodeGenerator() {
		// extends from MDDACodeGen
		_cg = new ClassGen(fullClassName, MDDACodeGen.class.getName(), simpleNameClass+".java", ACC_PUBLIC | ACC_SUPER, new String[] {  });
		_cp = _cg.getConstantPool();
		_factory = new InstructionFactory(_cg, _cp);
	}

	public void create(OutputStream out) throws IOException {
		createMethod_0();
	_cg.getJavaClass().dump(out);
	}

	private void createMethod_0() {
		InstructionList il = new InstructionList();
		// create consturctor
	    MethodGen method = new MethodGen(ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[] {  }, "<init>", fullClassName, il, _cp);
	
	    // call constructor
	    il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
	    il.append(_factory.createInvoke(MDDACodeGen.class.getName(), "<init>", Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL));
	
	    // DIMENSION (amount of dimension or biggest dimension)
	    il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
	    il.append(new PUSH(_cp, dimensions.length));
	    il.append(_factory.createFieldAccess(fullClassName, DIMENSION_SIZE_NAME, Type.INT, Constants.PUTFIELD));
	    
	    // DIMENSION_SIZE (each size of every dimension)
	    il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
	    il.append(new PUSH(_cp, dimensions.length)); // size of array
	    il.append(_factory.createNewArray(Type.INT, (short) 1)); // one dimension for dim size array 
	    for (int i=0; i<dimensions.length; i++) {
	    	il.append(InstructionConstants.DUP);
	        il.append(new PUSH(_cp, i));
	        il.append(new PUSH(_cp, dimensions[i]));
	        il.append(InstructionConstants.IASTORE);
	    }
	    il.append(_factory.createFieldAccess(fullClassName, DIMENSION_SIZE_ARRAY_NAME, new ArrayType(Type.INT, 1), Constants.PUTFIELD)); // one dimension for dim size array 
	    
	    
	    
	    // MULTI ARRAY
	    il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
	    // allocate the dimensions of the array
	    for (int i=0; i<dimensions.length; i++) {
	    	il.append(new PUSH(_cp, dimensions[i]));
	    }
	    if (PRIMITIVE) {
	    	 il.append(_factory.createNewArray(Type.DOUBLE, (short) dimensions.length)); // dimensions size
	    } else {
	    	 il.append(_factory.createNewArray(new ObjectType(arrayClass), (short) dimensions.length)); // dimensions size
	    }
	   
	    il.append(_factory.createFieldAccess(fullClassName, MULTI_DIMENSIONAL_ARRAY_NAME, Type.OBJECT, Constants.PUTFIELD));
	
	    il.append(InstructionFactory.createReturn(Type.VOID));
	    method.setMaxStack();
	    method.setMaxLocals();
	    _cg.addMethod(method.getMethod());
	    il.dispose();
	}

	public static String createMultArray(Class<?> clazz, int[] dimensions) throws FileNotFoundException, IOException {
		MDDACodeGenerator.dimensions = dimensions;
		String dimensionSizeAsString = StringUtils.join(ArrayUtils.toObject(dimensions));
		simpleNameClass = clazz.getSimpleName()+dimensions.length+"_"+dimensionSizeAsString;
		String packageName = MDDACodeGenerator.class.getPackage().getName()+".gen";
		arrayClass = clazz.getName();
		fullClassName = packageName + "." + simpleNameClass;
		if (DEBUG) {
			MDDACodeGenerator creator = new MDDACodeGenerator();
			// replace dots with slash
			String filePath = packageName.replaceAll("\\.", "/"); 
			filePath = TARGET_PATH + filePath + "/" + simpleNameClass;
			creator.create(new FileOutputStream(filePath + ".class"));
			generateSourceCode(clazz, packageName);
		} else {
			try {
				Class.forName(fullClassName);
			} catch (ClassNotFoundException e) {
				MDDACodeGenerator creator = new MDDACodeGenerator();
				// replace dots with slash
				String filePath = packageName.replaceAll("\\.", "/"); 
				FileUtils.forceMkdir(new File(TARGET_PATH+filePath));
				filePath = TARGET_PATH + filePath + "/" + simpleNameClass;
				creator.create(new FileOutputStream(filePath + ".class"));
				generateSourceCode(clazz, packageName);
			}
		}
		
		return fullClassName;
	}

	private static void generateSourceCode(Class<?> clazz, String packageName) {
		StringBuilder sb = new StringBuilder();
		sb.append("/**\n");
		sb.append("This file is auto generated by ");
		sb.append(MDDACodeGenerator.class.getName());
		sb.append(".\n");
		sb.append("It will not be compiled anytime! Its only a lookup for the byte code generation.\n");
		sb.append("*/");
		String prefix = "";
		// package
		sb.append("package ");
		sb.append(packageName);
		sb.append(";\n\n");

		sb.append("public class ");
		sb.append(simpleNameClass);
		sb.append(" extends ");
		sb.append(MDDACodeGen.class.getName());
		sb.append(" {");
		sb.append("\n");

		prefix = "  ";
		sb.append(prefix+"public ");
		sb.append(simpleNameClass);
		sb.append("()");
		sb.append(" {");
		sb.append("\n");

		prefix = "    ";
		// this.dimensionSize = new int[] { 2, 2 };
		sb.append(prefix+"this.");
		sb.append(DIMENSION_SIZE_ARRAY_NAME);
		sb.append(" = ");
		sb.append("new int[] {");
		sb.append(StringUtils.join(ArrayUtils.toObject(dimensions), ','));
		sb.append("};\n");
		sb.append("");

		// this.dimension = 2;
		sb.append(prefix+"this.");
		sb.append(DIMENSION_SIZE_NAME);
		sb.append(" = ");
		sb.append(dimensions.length);
		sb.append(";\n");

		// this.multiArray = new Double[2][2];

		sb.append(prefix+"this.");
		sb.append(MULTI_DIMENSIONAL_ARRAY_NAME);
		sb.append(" = ");
		sb.append(" new ");
		sb.append(clazz.getSimpleName());
		for (int i : dimensions) {
			sb.append("[");
			sb.append(i);
			sb.append("]");
		}
		sb.append(";\n");
		prefix = "  ";
		sb.append(prefix+"}\n");

		sb.append("}");

		try {
			File path = new File(SOURCE_GENERATED_PATH + simpleNameClass + ".java");
			FileUtils.writeStringToFile(path, sb.toString(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
