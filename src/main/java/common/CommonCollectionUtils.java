/*
 * Copyright 1999-2004 yihaodian.com All right reserved. This software is the confidential and proprietary information
 * of yihaodian.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of the license agreement you entered into with yihaodian.com.
 */
package common;

import common.constants.CacheConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * @author chenwu@yihaodian.com 2017年6月18日 下午6:58:01
 */
public class CommonCollectionUtils {

	private static final String EMPTY_STRING = ""; // 默认的空白字符串
	private static final String EMPTY_SEPERATOR = ","; // 默认的分隔符
	private static final String DEFAULT_EMBEDED_STRING = "'";// 默认的字符串包装符号

	/**
	 * 把{@link List}通过指定的参数seperator进行隔开<br/>
	 * 如果seperator为null或者空白字符串，则用默认的空白字符串进行隔开
	 * 
	 * @param list
	 * @param seperator
	 * @return {@link String}
	 * @author chenwu@yihaodian.com on 2014.10.14
	 */
	public static <T> String concatStrBySeperator(List<T> list, String seperator) {
		if (CollectionUtils.isEmpty(list)) {
			return EMPTY_STRING;
		}
		if (StringUtils.isEmpty(seperator)) {
			seperator = EMPTY_SEPERATOR;
		}
		StringBuffer sb = new StringBuffer();
		for (int index = 0; index < list.size(); index++) {
			T t = list.get(index);
			if (t != null) {
				sb.append(t.toString());
			}
			if (index < list.size() - 1) {
				sb.append(seperator);
			}
		}
		return sb.toString();
	}

	/**
	 * 将{@link List}按照分隔符seperator串联起来,同时每个字符串上再加上embededString(默认为单引号)包起来
	 * 
	 * @param list
	 *            字符串列表
	 * @param seperator
	 *            字符串之间的分隔符
	 * @param embededString
	 *            字符串两侧的包装符号
	 * @return {@link String}
	 * @author chenwu on 2017.7.21
	 */
	public static <T> String concatStrBySeperatorWithSingleQuote(List<T> list, String seperator, String embededString) {
		if (CollectionUtils.isEmpty(list)) {
			return EMPTY_STRING;
		}
		if (StringUtils.isEmpty(seperator)) {
			seperator = EMPTY_SEPERATOR;
		}
		if (StringUtils.isEmpty(embededString)) {
			embededString = DEFAULT_EMBEDED_STRING;
		}
		StringBuffer sb = new StringBuffer();
		for (int index = 0; index < list.size(); index++) {
			T t = list.get(index);
			if (t != null && !CacheConstants.NULL_OBJECT.equals(t)) {
				sb.append(embededString);
				sb.append(t.toString());
				sb.append(embededString);
				if (index < list.size() - 1) {
					sb.append(seperator);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 将字符串str按照seperator进行分割,形成{@link T}的list
	 * 
	 * @param str
	 * @param seperator
	 * @param clazz
	 *            转换的目标class类型
	 * @return List<T>
	 * @author chenwu@yihaodian.com on 2015.4.10
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> decodeStrBySeperator(String str, String seperator, Class<T> clazz) {
		if (StringUtils.isEmpty(str)) {
			return Collections.EMPTY_LIST;
		}
		if (StringUtils.isEmpty(seperator)) {
			seperator = EMPTY_SEPERATOR;
		}
		List<T> result = new ArrayList<T>();
		String[] array = str.split(seperator);
		for (String item : array) {
			if (String.class.equals(clazz)) {
				// 表明泛化类型是string类型
				result.add((T) item);
			} else if (Number.class.isAssignableFrom(clazz)) {
				// 如果Number是clazz的超类
				try {
					Constructor<T> constructorFromString = clazz.getDeclaredConstructor(String.class);
					T newItem = constructorFromString.newInstance(item);
					result.add(newItem);
				} catch (Exception e) {
					// ignore the reflected exception
				}
			} else {
				throw new RuntimeException("don't support the class type:" + clazz.getSimpleName());
			}
		}
		return result;
	}

	/**
	 * @Description: 根据给定起始位置截取list @param @param <T> @param @param src
	 *               源list @param @param start 截取开始位置 @param @param end
	 *               截取结束位置 @param @return 设定文件 @return List<T> 返回类型 @author
	 *               wanghui3 @date 2013-7-15 @throws
	 */
	public static <T> List<T> subListWithLocation(List<T> src, int start, int end) {
		if (src == null || src.isEmpty() || end <= start || start >= src.size()) {
			return Collections.emptyList();
		}
		int tempStart = start;
		int tempEnd = end;
		int size = src.size();
		if (end > size) {
			tempEnd = size;
		}
		if (start < 0) {
			tempStart = 0;
		}
		return new ArrayList<T>(src.subList(tempStart, tempEnd));
	}

	/**
	 * 交替组合列表数据并去除重复<br/>
	 * 
	 * <pre>
	 *  Map<Integer, List<Long>> mapList = new HashMap<Integer,
	 * List<Long>>(); mapList.put(1, Arrays.asList(1l,2l,3l,4l)); mapList.put(2,
	 * Arrays.asList(4l,5l,6l,7l)); List<Long> result =
	 * CommonCollectionUtil.assembledListAndFilterRepeat(mapList, 6, 3);
	 * 这里返回的result为[1,2,3,4,5,6]
	 * </pre>
	 * 
	 * @Title: assembledListAndFilterRepeat @Description: @param mapList @param
	 *         resultSize 最终list长度 @param preSize map内每个list每次拉取的数量 @return List
	 *         <Long> @author wuxiang1 @date 2015-4-21 @throws
	 */
	public static List<Long> assembledListAndFilterRepeat(Map<Integer, List<Long>> mapList, int resultSize,
			int preSize) {
		int maxListSize = 0;
		for (Map.Entry<Integer, List<Long>> entry : mapList.entrySet()) {
			List<Long> idList = entry.getValue();
			if (!CollectionUtils.isEmpty(idList) && idList.size() > maxListSize) {
				maxListSize = idList.size();
			}
		}
		Set<Long> idSet = new LinkedHashSet<Long>();
		int cycleNums = (int) Math.ceil((double) maxListSize / (double) preSize);
		for (int i = 0; i < cycleNums; i++) {
			int fromIndex = i * preSize;
			int toIndex = (i + 1) * preSize;
			for (Map.Entry<Integer, List<Long>> entry : mapList.entrySet()) {
				List<Long> idList = entry.getValue();
				if (!CollectionUtils.isEmpty(idList) && fromIndex <= idList.size()) {
					if (toIndex > idList.size()) {
						toIndex = idList.size();
					}
					idSet.addAll(idList.subList(fromIndex, toIndex));
				}
			}
		}
		ArrayList<Long> result = new ArrayList<Long>(idSet);
		if (result.size() >= resultSize) {
			return result.subList(0, resultSize);
		} else {
			return result;
		}
	}

	/**
	 * 交替组合列表数据并去除重复<br/>
	 * 
	 * <pre>
	 *          Map<Integer, List<Long>> mapList = new HashMap<Integer, List<Long>>();
	 *         mapList.put(1, Arrays.asList(1l,2l,3l,4l));
	 *         mapList.put(2, Arrays.asList(4l,5l,6l,7l));
	 *         Set<Long> result = CommonCollectionUtil.assembledListAndFilterRepeat(mapList, 4);
	 *         这里返回的result为[1,4,2,5]
	 * </pre>
	 * 
	 * @param map
	 *            列表map
	 * @param loopTime
	 *            抽取个数
	 * @return Set<T> 返回组合后的数据
	 * @author chenwu@yihaodian.com on 2015.5.20
	 */
	public static <K, T> Set<T> assembledListAndFilterRepeat(Map<K, List<T>> map, int loopTime) {
		if (null == map || map.isEmpty() || loopTime <= 0) {
			return Collections.emptySet();
		}

		Queue<T> queue = null;

		Set<T> polledItem = new LinkedHashSet<T>();

		Map<K, Queue<T>> queueMap = new LinkedHashMap<K, Queue<T>>();

		Set<Map.Entry<K, List<T>>> entrySet = map.entrySet();
		for (Map.Entry<K, List<T>> entry : entrySet) {
			List<T> ids = entry.getValue();
			if (CollectionUtils.isEmpty(ids)) {
				continue;
			}
			queue = new ArrayBlockingQueue<T>(ids.size());
			queue.addAll(ids);
			queueMap.put(entry.getKey(), queue);
		}

		T element = null;
		int validRecommendItem = 0;
		Set<Map.Entry<K, Queue<T>>> queueEntrySet = queueMap.entrySet();
		for (;;) {
			if (queueMap.isEmpty()) {
				return polledItem;
			}

			for (Iterator<Map.Entry<K, Queue<T>>> iter = queueEntrySet.iterator(); iter.hasNext();) {
				if (validRecommendItem >= loopTime) {
					return polledItem;
				}
				Map.Entry<K, Queue<T>> entry = iter.next();
				K key = entry.getKey();
				element = poll(queueMap.get(key), polledItem);
				if (element == null) {
					iter.remove();
				} else {
					validRecommendItem++;
				}
			}
		}
	}

	/**
	 * 从队列里获取不重复的元素
	 * 
	 * @param queue
	 *            队列
	 * @param polledItem
	 *            已有的元素集合
	 * @return 目标元素
	 */
	private static <T> T poll(Queue<T> queue, Set<T> polledItem) {
		if (queue.isEmpty()) {
			return null;
		}
		T element = queue.poll();
		// 如果队列中弹出的元素已存在，则获取下一个元素
		if (polledItem.contains(element)) {
			return poll(queue, polledItem);
		}
		// 否则，加入已有的元素集合，并返回目标元素
		else {
			polledItem.add(element);
			return element;
		}
	}

	/**
	 * 完全打散idMap里的value值
	 * 
	 * @param idMap
	 * @return Map<Long, List<T>>
	 * @author chenwu@yihaodian.com on 2015.5.20
	 */
	public static <T> Map<Long, List<T>> shuffleIdListOfMap(Map<Long, List<T>> idMap) {
		if (MapUtils.isEmpty(idMap)) {
			return null;
		}
		Map<Long, List<T>> resultMap = new HashMap<Long, List<T>>();
		for (Map.Entry<Long, List<T>> entry : idMap.entrySet()) {
			if (CollectionUtils.isEmpty(entry.getValue())) {
				continue;
			}
			List<T> list = new ArrayList<T>(entry.getValue().size());
			Collections.copy(list, entry.getValue());
			Collections.shuffle(list);
			resultMap.put(entry.getKey(), list);
		}
		return resultMap;
	}

	/**
	 * 对源参数列表对象{@link List}去重后再返回
	 * 
	 * @param list
	 * @return {@link List}
	 * @throws Exception
	 * @author chenwu on 2017.7.26
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getDistinctList(List<T> list) {
		if (CollectionUtils.isEmpty(list)) {
			return Collections.EMPTY_LIST;
		}
		Set<T> set = new HashSet<T>(list);
		List<T> distinctList = new ArrayList<T>(set);
		return distinctList;
	}

	/**
	 * 根据第一维度的key,第二维度的key去二维表{@link Map}里查询
	 * 
	 * @param oneDimenKey
	 * @param secDimenKey
	 * @param map
	 * @return {@link T}
	 * @author chenwu on 2018.7.18
	 */
	public static <T> T queryValueFromTwoDimenMap(String oneDimenKey, String secDimenKey,
			Map<String, Map<String, T>> map) {
		if (MapUtils.isEmpty(map)) {
			return null;
		}
		Map<String, T> secDimenMap = map.get(oneDimenKey);
		if (MapUtils.isEmpty(secDimenMap)) {
			return null;
		}
		T t = secDimenMap.get(secDimenKey);
		return t;
	}

	/**
	 * 根据一维、二维维度的key,将{@link T}的值放入到参数map里
	 * 
	 * @param oneDimenKey
	 * @param secDimenKey
	 * @param newValue
	 * @param map
	 * @author chenwu on 2018.7.18
	 */
	public static <T> void putValueByTwoDimenStr(String oneDimenKey, String secDimenKey, T newValue,
			Map<String, Map<String, T>> map) {
		if (map == null) {
			return;
		}
		Map<String, T> secDimenMap = map.get(oneDimenKey);
		if (MapUtils.isEmpty(secDimenMap)) {
			secDimenMap = new HashMap<String, T>();
			map.put(oneDimenKey, secDimenMap);
		}
		secDimenMap.put(secDimenKey, newValue);
	}

}
